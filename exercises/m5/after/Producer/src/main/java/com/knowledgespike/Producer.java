package com.knowledgespike;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Producer {

    public final static String QUEUE_NAME = "rpc";

    private final Connection connection;
    private Channel channel;
    private String replyTo;
    private String correlationId;
    private AMQP.BasicProperties properties;

    public Producer(String host, String name) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection(name);
    }

    private void createChannel() throws IOException {
        channel = connection.createChannel();
    }

    private void declareQueue() throws IOException {
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
    }

    private void createTemporaryQueue() throws IOException {
        replyTo = channel.queueDeclare().getQueue();
    }

    private void createCorrelationId() {
        correlationId = UUID.randomUUID().toString();
    }

    private void buildProperties() {
        properties = new AMQP.BasicProperties.Builder()
                .correlationId(correlationId)
                .replyTo(replyTo)
                .build();
    }

    private void publishMessage(String message) throws IOException {
        channel.basicPublish("", QUEUE_NAME, properties, message.getBytes(StandardCharsets.UTF_8));
        System.out.println(" [x] Sent '" + message + "'");
    }

    private void consumeResponse() throws IOException {
        channel.basicConsume(replyTo, true, ((consumerTag, message) -> {
            if (message.getProperties().getCorrelationId().equals(correlationId)) {
                var received = new String(message.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received a response");
                connection.close();
            }
        }), consumerTag -> {
        });
    }

    private void closeConnection() throws IOException {
        connection.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException {

        var producer = new Producer("localhost", "app:prodcuer");

        producer.createChannel();
        producer.declareQueue();
        producer.createTemporaryQueue();
        producer.createCorrelationId();
        producer.buildProperties();
        producer.publishMessage("Simple message");
        producer.consumeResponse();
//        producer.closeConnection();
    }

}
