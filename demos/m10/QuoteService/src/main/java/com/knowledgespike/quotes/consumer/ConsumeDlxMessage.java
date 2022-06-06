package com.knowledgespike.quotes.consumer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConsumeDlxMessage  {

    private static final Logger logger
            = LoggerFactory.getLogger(ConsumeDlxMessage.class);

    Channel channel;
    private final String queueName;

    public ConsumeDlxMessage(Connection connection, String dlExchangeName, String queueName) throws  IOException {

        this.queueName = queueName;

        channel = connection.createChannel();
        channel.exchangeDeclare(dlExchangeName, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, dlExchangeName, "user-dlx");

    }

    public void consumeMessage(DeliverCallback callback) {
        try {
            channel.basicConsume(queueName, true, callback, (t) -> {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }


}
