package com.knowledgespike.quotes.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.messages.LoanResponseMessage;
import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.AMQBasicProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ConsumeLoanMessage {
    private static final String QUEUE_NAME = "loanApplication";

    private final Channel channel;

    public ConsumeLoanMessage(Connection connection) throws IOException {
        channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
    }

    public void consumeMessage(DeliverCallback deliverCallback, CancelCallback cancelCallback) {
        try {
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean reply(LoanResponseMessage response, String replyTo, String correlationId, long deliveryTag) {
        try {

            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(correlationId)
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            String message = mapper.writeValueAsString(response);

            System.out.println(" [x] About to reply");

            channel.basicPublish("", replyTo, props, message.getBytes(StandardCharsets.UTF_8));

            channel.basicAck(deliveryTag, false);
            return true;

        } catch (Exception e) {
            System.out.println(" [x] Unable to reply");
            return false;
        }
    }
}
