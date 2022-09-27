package com.knowledgespike;

import com.rabbitmq.client.AMQP;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private Channel channel;


    public Consumer(Channel channel) {
        this.channel = channel;
    }

    public void sendResponseMessage(String replyTo, String correlationId, long deliveryTag) throws IOException, TimeoutException {
//        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
//                .correlationId(correlationId)
//                .build();

        System.out.println(" [x] About to reply");
        channel.basicPublish("", replyTo, null, "reply".getBytes(StandardCharsets.UTF_8));
        channel.basicAck(deliveryTag, false);
    }
}
