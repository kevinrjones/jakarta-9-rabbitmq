package com.knowledgespike;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class Producer {

    private final static String EXCHANGE_NAME = "headers-exchange";
    private final Connection connection;
    private Channel channel;
    private HashMap<String, Object> headers;
    private AMQP.BasicProperties properties;


    public Producer(String host, String name) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection(name);
    }

    private void createChannel() throws IOException {
        channel = connection.createChannel();
    }

    private void declareExchange() throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.HEADERS);
    }

    private void createMessageHeaders() {
        headers = new HashMap<String, Object>();

        headers.put("message-type", "log");
        headers.put("message-level", "info");
    }

    private void createProperties() {
        properties = new AMQP.BasicProperties.Builder()
                .headers(headers)
                .build();
    }

    private void closeConnection() throws IOException {
        connection.close();
    }


    private void publishMessage(String message) throws IOException {
        channel.basicPublish(EXCHANGE_NAME, "", properties, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "'");
    }

    public static void main(String[] args) throws IOException, TimeoutException {

        var producer = new Producer("localhost", "app:prodcuer");

        producer.createChannel();
        producer.declareExchange();
        producer.createMessageHeaders();
        producer.createProperties();
        producer.publishMessage("Simple message to headers exchange");
        producer.closeConnection();
    }

}
