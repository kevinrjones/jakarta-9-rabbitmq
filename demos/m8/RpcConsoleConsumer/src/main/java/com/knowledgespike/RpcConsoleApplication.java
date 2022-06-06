package com.knowledgespike;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.consumer.ConsumeLoanMessage;
import com.knowledgespike.quotes.messages.LoanApplicationMessage;
import com.knowledgespike.quotes.messages.LoanResponseMessage;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RpcConsoleApplication {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection("app:api:client");

        var consumeLoanMessage = new ConsumeLoanMessage(connection);

        CancelCallback cancelCallback = (consumerTag) -> System.out.println(" [x] Queue has been cancelled");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();

                LoanApplicationMessage loanApplicationMessage = mapper.readValue(message, LoanApplicationMessage.class);
                System.out.println(" [x] Consumed '" + loanApplicationMessage);

                var response = new LoanResponseMessage(true);

                if(!consumeLoanMessage.reply(response, delivery.getProperties().getReplyTo(), delivery.getProperties().getCorrelationId(), delivery.getEnvelope().getDeliveryTag())) {
                    System.out.println(" [x] Unable to respond to server");
                }

            } catch (Exception e) {
                System.out.println(" [x] " + e);
            }

        };


        consumeLoanMessage.consumeMessage(deliverCallback, cancelCallback);
    }
}
