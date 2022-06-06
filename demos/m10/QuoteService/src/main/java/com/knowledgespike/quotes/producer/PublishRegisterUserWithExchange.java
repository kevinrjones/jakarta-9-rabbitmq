package com.knowledgespike.quotes.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.messages.UserRegistrationMessage;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;


public class PublishRegisterUserWithExchange {
    private static final Logger logger
            = LoggerFactory.getLogger(PublishRegisterUserWithExchange.class);

    private final static String EXCHANGE_NAME = "direct-user";
    private final static String ROUTING_KEY = "direct-route";
    private final static String ANOTHER_ROUTING_KEY = "another-direct-route";
    private final static Map<String, Object> arguments = null;
    private Connection connection;

    public PublishRegisterUserWithExchange(Connection connection) {
        this.connection = connection;
    }

    public boolean sendMessage(UserRegistrationMessage message) {

        try (var channel = connection.createChannel()) {
            channel.exchangeDeclare("direct-user", BuiltinExchangeType.DIRECT);

            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            String userRegJson = mapper.writeValueAsString(message);

            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, properties, userRegJson.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Publish '" + userRegJson + "'");
            channel.basicPublish(EXCHANGE_NAME, ANOTHER_ROUTING_KEY, properties, userRegJson.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Publish '" + userRegJson + "'");
            return true;
        } catch (Exception e) {
            logger.error("Unable to publish message", e);
            return false;
        }
    }

}
