package com.knowledgespike;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private final static String EXCHANGE_NAME = "fanout_exchange";
    Connection connection;
    Channel channel;
    String message;
    private String queueName;

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

    private void declareExchange() throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    }

    private void declareQueue() throws IOException {
        queueName = channel.queueDeclare().getQueue();
    }

    private void bindQueue() throws IOException {
        channel.queueBind(queueName, EXCHANGE_NAME, "");
    }

    private void consumeMessage() throws IOException {
        channel.basicConsume(queueName, true, deliverCallback, (CancelCallback) null);
    }

    DeliverCallback deliverCallback = (String consumerTag, Delivery delivery) -> {
        message = new String(delivery.getBody(), StandardCharsets.UTF_8);

        System.out.println(" [x] Fanout message: " + message);

        // optional
        connection.close();
    };


    public static void main(String[] args) throws IOException, TimeoutException {

        var consumer = new Consumer("localhost", "simple:client");

        consumer.createChannel();
        consumer.declareExchange();
        consumer.declareQueue();
        consumer.bindQueue();
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        consumer.consumeMessage();
    }
}
