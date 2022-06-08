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


    public Producer(String host, String name)  {;
    }


    public static void main(String[] args) throws IOException, TimeoutException {

        var producer = new Producer("localhost", "app:prodcuer");

//        producer.createChannel();
//        producer.declareQueue();
//        producer.createTemporaryQueue();
//        producer.createCorrelationId();
//        producer.buildProperties();
//        producer.publishMessage("Simple message");
//        producer.consumeResponse();
    }

}
