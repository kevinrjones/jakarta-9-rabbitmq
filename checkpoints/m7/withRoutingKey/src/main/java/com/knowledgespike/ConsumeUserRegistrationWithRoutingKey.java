package com.knowledgespike;

import com.rabbitmq.client.BuiltinExchangeType;

import java.io.IOException;

public class ConsumeUserRegistrationWithRoutingKey {

    private Channel channel;


    public ConsumeUserRegistrationWithRoutingKey(Channel channel, String exchangeName, String queueName, String routingKey) {

        try {
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
