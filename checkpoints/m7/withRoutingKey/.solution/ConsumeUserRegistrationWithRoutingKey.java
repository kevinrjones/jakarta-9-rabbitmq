package com.knowledgespike;

import com.rabbitmq.client.BuiltinExchangeType;

import java.io.IOException;

public class ConsumeUserRegistrationWithRoutingKey {

    private Channel channel;


    public ConsumeUserRegistrationWithRoutingKey(Channel channel, String exchangeName, String queueName, String routingKey) {

        try {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
            channel.queueDeclare(queueName, true, false, true, null);
            channel.queueBind(queueName, exchangeName, routingKey, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
