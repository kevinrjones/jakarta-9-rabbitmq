package com.knowledgespike;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.QuoteException;
import com.knowledgespike.quotes.consumer.ConsumeUserRegistration;
import com.knowledgespike.quotes.messages.UserRegistrationMessage;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ResisterUserPollConsoleApplication {
    private final static String QUEUE_NAME = "users";

    public static void main(String[] args) throws QuoteException, IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection("app:api:client");


        var consumeUserRegistration = new ConsumeUserRegistration(connection);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);

            ObjectMapper mapper = new ObjectMapper();

            UserRegistrationMessage userRegistrationMessage = mapper.readValue(message, UserRegistrationMessage.class);

            System.out.println(" [x] User registration message: " + userRegistrationMessage);
        };


        while (!consumeUserRegistration.pollQueue(deliverCallback)) {
            try {
                System.out.println(" [x] waiting ...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }

        System.out.println(" [x] Finished waiting");

        consumeUserRegistration.close();


    }
}
