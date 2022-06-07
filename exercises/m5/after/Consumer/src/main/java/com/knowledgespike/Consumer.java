package com.knowledgespike;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private final static String QUEUE_NAME = "rpc";

    Connection connection;
    Channel channel;
    String message;

    public Consumer(String host, String name) throws IOException, TimeoutException {
        connection = connect(host, name);
    }

    private Connection connect(String host, String name) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        return factory.newConnection(name);
    }

    private void createChannel() throws IOException {
        channel = connection.createChannel();
    }

    private void declareQueue() throws IOException {
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
    }

    private void consumeMessage() throws IOException {
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, (CancelCallback) null);
    }

    DeliverCallback deliverCallback = (String consumerTag, Delivery delivery) -> {
        message = new String(delivery.getBody(), StandardCharsets.UTF_8);

        System.out.println(" [x] Direct message: " + message);

        sendResponseMessage(delivery.getProperties().getReplyTo(), delivery.getProperties().getCorrelationId(), delivery.getEnvelope().getDeliveryTag());
        connection.close();
    };

    private void sendResponseMessage(String replyTo, String correlationId, long deliveryTag) throws IOException {
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(correlationId)
                .build();

        System.out.println(" [x] About to reply");
        channel.basicPublish("", replyTo, props, "reply".getBytes(StandardCharsets.UTF_8));
        channel.basicAck(deliveryTag, false);
    }


    public static void main(String[] args) throws IOException, TimeoutException {

        var consumer = new Consumer("localhost", "simple:client");

        consumer.createChannel();
        consumer.declareQueue();
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        consumer.consumeMessage();
    }
}
