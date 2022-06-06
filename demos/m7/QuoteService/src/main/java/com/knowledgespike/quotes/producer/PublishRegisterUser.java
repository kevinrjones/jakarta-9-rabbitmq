package com.knowledgespike.quotes.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.messages.UserRegistrationMessage;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class PublishRegisterUser {
    private static final Logger logger
            = LoggerFactory.getLogger(PublishRegisterUser.class);

    private Connection connection;
    public final static String QUEUE_NAME = "users";


    public PublishRegisterUser(Connection connection) {
        this.connection = connection;
    }

    public boolean sendMessage(UserRegistrationMessage message) {
        try(var channel = connection.createChannel()) {

            ObjectMapper mapper = new ObjectMapper();
            String userRegJson = mapper.writeValueAsString(message);

            channel.basicPublish("", QUEUE_NAME, null, userRegJson.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Sent '" + userRegJson + "'");

            return true;
        } catch (IOException | TimeoutException e) {
            logger.error("Unable to publish message", e);
            return false;
        }

    }
}
