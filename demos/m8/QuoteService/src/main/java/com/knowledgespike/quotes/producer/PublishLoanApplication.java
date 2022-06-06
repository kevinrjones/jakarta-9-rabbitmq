package com.knowledgespike.quotes.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.messages.LoanApplicationMessage;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class PublishLoanApplication {

    private static final String QUEUE_NAME = "loanApplication";
    private Connection connection;

    public PublishLoanApplication(Connection connection) {
        this.connection = connection;
    }

    public String sendMessage(LoanApplicationMessage loanApplicationMessage) {
        try (var channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, true, null);

            var replyTo = channel.queueDeclare().getQueue();

            final String corrId = UUID.randomUUID().toString();

            ObjectMapper mapper = new ObjectMapper();
            String loanApplicationJson = mapper.writeValueAsString(loanApplicationMessage);

            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .correlationId(corrId)
                    .replyTo(replyTo)
                    .build();


            channel.basicPublish("", QUEUE_NAME, properties, loanApplicationJson.getBytes(StandardCharsets.UTF_8));

            System.out.println(" [x] Published a message");

            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);

            channel.basicConsume(replyTo, true, ((consumerTag, message) -> {
                if (message.getProperties().getCorrelationId().equals(corrId)) {
                    var received = new String(message.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Received a response");
                    response.offer(received);
                }
            }), consumerTag -> {
            });

            return response.take();
        } catch (IOException | TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}








