package com.knowledgespike;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.consumer.ConsumeDlxMessage;
import com.knowledgespike.quotes.consumer.DlxConsumeUserRegistration;
import com.knowledgespike.quotes.messages.UserRegistrationMessage;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class DlxConsoleApplication {

    private final static String DLX_QUEUE_NAME = "user_queue_dlx";
    private final static String DLX_EXCHANGE_NAME = "user_dlx";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConsumeInitialMessages();
        ConsumeDlxMessages();
    }

    private static void ConsumeDlxMessages() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection("app:api:client");

        var consumeDlxMessage = new ConsumeDlxMessage(connection, DLX_EXCHANGE_NAME, DLX_QUEUE_NAME);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received dead letter message'" + message + "' with tag '" + consumerTag + "'");
            ObjectMapper mapper = new ObjectMapper();

            UserRegistrationMessage userRegistrationMessage = mapper.readValue(message, UserRegistrationMessage.class);

            System.out.println(" [x] Dead Letter message: " + userRegistrationMessage);

        };

        consumeDlxMessage.consumeMessage(deliverCallback);
    }

    private static void ConsumeInitialMessages() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection("app:api:client");

        DlxConsumeUserRegistration consumeUserRegistration = new DlxConsumeUserRegistration(connection);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(" [x] Queue has been cancelled");

        };

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            var rnd = new Random();

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "' with tag '" + consumerTag + "'");
            ObjectMapper mapper = new ObjectMapper();

            UserRegistrationMessage userRegistrationMessage = mapper.readValue(message, UserRegistrationMessage.class);

            System.out.println(" [x] User registration message: " + userRegistrationMessage);

            if (!rnd.nextBoolean())
                consumeUserRegistration.acknowledgeMessage(delivery.getEnvelope().getDeliveryTag());
            else
                consumeUserRegistration.nAcknowledgeMessage(delivery.getEnvelope().getDeliveryTag());


        };

        consumeUserRegistration.consumeMessage(deliverCallback, cancelCallback);
    }
}
