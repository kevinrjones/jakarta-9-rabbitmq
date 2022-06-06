package com.knowledgespike.quotes.consumer;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DlxConsumeUserRegistration  {

    private static final Logger logger
            = LoggerFactory.getLogger(ConsumeUserRegistration.class);
    public final static String QUEUE_NAME = "users";
    public final static String DLX_EXCHANGE_NAME = "user_dlx";

    Channel channel;

    public DlxConsumeUserRegistration(Connection connection) throws IOException {

        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DLX_EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", "user-dlx");

        channel = connection.createChannel();
        channel.exchangeDeclare(DLX_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(QUEUE_NAME, false, false, false, args);

    }

    public void consumeMessage(DeliverCallback callback, CancelCallback cancelCallback) {
        try {
            channel.basicConsume(QUEUE_NAME, false, callback, cancelCallback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    public void acknowledgeMessage(long messageTag) {
        try {
            logger.debug("Acknowledge message");
            channel.basicAck(messageTag, false);
        } catch (Exception e) {
            logger.error("Unable to acknowledge message", e);
            throw new RuntimeException(e);
        }
    }

    public void nAcknowledgeMessage(long messageTag) {
        try {
            logger.debug("Non-Acknowledge message");
            channel.basicNack(messageTag, false, false);
        } catch (Exception e) {
            logger.error("Unable to nAcknowledge message", e);
            throw new RuntimeException(e);
        }
    }
}
