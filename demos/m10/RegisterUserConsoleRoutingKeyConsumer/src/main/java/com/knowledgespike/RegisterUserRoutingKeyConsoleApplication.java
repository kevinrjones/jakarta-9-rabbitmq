package com.knowledgespike;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.consumer.ConsumeUserRegistrationWithRoutingKey;
import com.knowledgespike.quotes.messages.UserRegistrationMessage;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RegisterUserRoutingKeyConsoleApplication {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection("app:api:client");



        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        CancelCallback cancelCallback = (consumerTag) -> {
            System.out.println(" [x] Queue has been cancelled");
        };

        var consumerUserRegistration1 = new ConsumeUserRegistrationWithRoutingKey(connection, "direct-queue-d", "direct-route", true);
        var consumerUserRegistration2 = new ConsumeUserRegistrationWithRoutingKey(connection, "direct-queue-nd", "another-direct-route", false);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            var properties = delivery.getProperties();

            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "' with tag '" + consumerTag + "'");
            ObjectMapper mapper = new ObjectMapper();

            UserRegistrationMessage userRegistrationMessage = mapper.readValue(message, UserRegistrationMessage.class);

            System.out.println(" [x] User registration message: " + userRegistrationMessage);

            consumerUserRegistration1.acknowldge(delivery.getEnvelope().getDeliveryTag());
            consumerUserRegistration2.acknowldge(delivery.getEnvelope().getDeliveryTag());
        };


        consumerUserRegistration1.consumeMessage(deliverCallback, cancelCallback);


        consumerUserRegistration2.consumeMessage(deliverCallback, cancelCallback);


    }
}
