package com.knowledgespike;

import com.rabbitmq.client.AMQP;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface Channel {
    void basicPublish(String exchange, String queue, AMQP.BasicProperties properties, byte[] message) throws IOException, TimeoutException;

    void basicConsume(String queueName, boolean autoAck, DeliverCallback callback, CancelCallback cancelCallback) throws IOException;

    void basicAck(long deliveryTag, boolean multiple);
}
