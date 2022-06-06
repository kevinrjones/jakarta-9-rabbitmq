package com.knowledgespike.quotes.consumer;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ConsumeUserRegistrationWithRoutingKey {
    private static final Logger logger
            = LoggerFactory.getLogger(ConsumeUserRegistrationWithRoutingKey.class);

    public final static String ROUTING_KEY = "direct-route";

    private static final String QUEUE_NAME = "direct-queue";

    private static String EXCHANGE_NAME = "direct-exchange";

    private final Channel channel;

    public ConsumeUserRegistrationWithRoutingKey(Connection connection) throws IOException {
        channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, true, true, true, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
    }

    public void consumeMessage(DeliverCallback deliverCallback, CancelCallback cancel) {
        try {
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancel);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}












