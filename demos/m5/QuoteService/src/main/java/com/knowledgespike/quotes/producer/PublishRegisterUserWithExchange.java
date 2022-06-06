package com.knowledgespike.quotes.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.messages.UserRegistrationMessage;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class PublishRegisterUserWithExchange {

    private static final Logger logger
            = LoggerFactory.getLogger(PublishRegisterUserWithExchange.class);

    private static String EXCHANGE_NAME = "direct-exchange";
    private static String ROUTING_KEY = "direct-route";
    private Connection connection;

    public PublishRegisterUserWithExchange(Connection connection) {
        this.connection = connection;
    }

    public boolean sendMessage(UserRegistrationMessage message) {
        try (var channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            ObjectMapper mapper = new ObjectMapper();
            String userRegJson = mapper.writeValueAsString(message);

            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, userRegJson.getBytes(StandardCharsets.UTF_8));

            logger.info(" [x] Publish '" + userRegJson + "'");

            return true;
        } catch (Exception e) {
            logger.error("Unable to publish message", e);
            return false;
        }
    }
}


