package com.knowledgespike;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {

    public Producer(String host, String name)  {
    }


    public static void main(String[] args) throws IOException, TimeoutException {

//        var producer = new Producer("", "");
//
//        producer.createChannel();
//        producer.declareExchange();
//        producer.publishMessage("Simple message to '" + ROUTING_KEY + "'", ROUTING_KEY);
//        producer.publishMessage("Simple message to'" + ANOTHER_ROUTING_KEY + "'", ANOTHER_ROUTING_KEY);
//        producer.closeConnection();
    }

}
