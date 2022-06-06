package com.knowledgespike.quotes.consumer;

import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ConsumeUserRegistrationWithRoutingKey {

    private static final Logger logger
            = LoggerFactory.getLogger(ConsumeUserRegistrationWithRoutingKey.class);
//    public final static String ROUTING_KEY = "direct-route";

    Channel channel;
    private String queueName;
//    private static final String QUEUE_NAME = "direct-queue";

    private static final String EXCHANGE_NAME = "direct-user";

    public ConsumeUserRegistrationWithRoutingKey(Connection connection, String queueName, String routingKey) throws IOException {


        // can create the queue this way with a name and bind it
        channel = connection.createChannel();
        this.queueName = queueName;
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(queueName, true, false, true, null);
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey, null);
    }

    public void consumeMessage(DeliverCallback callback, CancelCallback cancelCallback) {
        try {
            channel.basicConsume(queueName, true, callback, cancelCallback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

}
