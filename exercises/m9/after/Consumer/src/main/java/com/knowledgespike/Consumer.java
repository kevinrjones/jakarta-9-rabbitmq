package com.knowledgespike;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private final static String EXCHANGE_NAME = "headers-exchange";
    private final static String QUEUE_NAME = "headers-queue";

    Connection connection;
    Channel channel;
    String message;
    private HashMap<String, Object> headers;

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
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);
    }

    private void createMessageHeaders() {
        headers = new HashMap<String, Object>();

        headers.put("x-match", "all");

        headers.put("message-type", "log");
        headers.put("message-level", "info");
    }

    private void declareQueue() throws IOException {
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
    }

    private void bindQueue() throws IOException {
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "", headers);
    }

    private void consumeMessage() throws IOException {
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, (CancelCallback) null);
    }

    DeliverCallback deliverCallback = (String consumerTag, Delivery delivery) -> {
        message = new String(delivery.getBody(), StandardCharsets.UTF_8);

        System.out.println(" [x] Topic message: " + message);

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
