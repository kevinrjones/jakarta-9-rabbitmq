package com.knowledgespike;

import com.rabbitmq.client.BuiltinExchangeType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PublishRegisterUserWithExchange {

    private Channel channel;


    public PublishRegisterUserWithExchange(Channel channel) {
        this.channel = channel;
    }

    public boolean sendMessage(String message) {
        try {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
