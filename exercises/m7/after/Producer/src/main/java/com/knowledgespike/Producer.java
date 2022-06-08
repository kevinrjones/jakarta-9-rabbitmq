package com.knowledgespike;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {

    private final static String EXCHANGE_NAME = "direct-exchange";
    private final static String ROUTING_KEY = "direct-route";
    private final static String ANOTHER_ROUTING_KEY = "another-direct-route";

    private final Connection connection;
    private Channel channel;

    public Producer(String host, String name) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection(name);
    }

    private void createChannel() throws IOException {
        channel = connection.createChannel();
    }

    private void declareExchange() throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
    }

    private void closeConnection() throws IOException {
        connection.close();
    }


    private void publishMessage(String message, String routingKey) throws IOException {
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "'");
    }

    public static void main(String[] args) throws IOException, TimeoutException {

        var producer = new Producer("localhost", "app:prodcuer");

        producer.createChannel();
        producer.declareExchange();
        producer.publishMessage("Simple message", ROUTING_KEY);
        producer.publishMessage("Simple message", ANOTHER_ROUTING_KEY);
        producer.closeConnection();
    }

}
