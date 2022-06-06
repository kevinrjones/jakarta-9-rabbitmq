package com.knowledgespike.quotes.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.messages.UserRegistrationMessage;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PublishRegisterUserDlx {
    private static final Logger logger
            = LoggerFactory.getLogger(PublishRegisterUser.class);

    public final static String QUEUE_NAME = "users";
    private final static String EXCHANGE_NAME = "";
    private final static String DLX_EXCHANGE_NAME = "user_dlx";

    private final static Map<String, Object> arguments = null;
    private final Connection connection;

    public PublishRegisterUserDlx(Connection connection) {
        this.connection = connection;
    }

    public boolean sendMessage(UserRegistrationMessage message) {
        return sendMessage(message, "text/json", "");
    }

    public boolean sendMessage(UserRegistrationMessage message, String contentType, String contentEncoding) {

        try (var channel = connection.createChannel()) {

            Map<String, Object> args = new HashMap<>();
            args.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
            args.put("x-dead-letter-routing-key", "user-dlx");

            channel.exchangeDeclare(DLX_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            channel.queueDeclare(QUEUE_NAME, false, false, false, args);

            ObjectMapper mapper = new ObjectMapper();
            String userRegJson = mapper.writeValueAsString(message);

            channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, userRegJson.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Sent '" + userRegJson + "'");
            return true;
        } catch (Exception e) {
            logger.error("Unable to publish message", e);
            return false;
        }
    }

}
