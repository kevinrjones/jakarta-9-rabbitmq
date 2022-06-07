package com.knowledgespike;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {

    public final static String QUEUE_NAME = "direct";

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

    private void closeConnection() throws IOException {
        connection.close();
    }


    private void publishMessage(String message) throws IOException {
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "'");
    }

    public static void main(String[] args) throws IOException, TimeoutException {

        var producer = new Producer("localhost", "app:prodcuer");

        producer.createChannel();
        producer.publishMessage("Simple message");
        producer.closeConnection();
    }

}
