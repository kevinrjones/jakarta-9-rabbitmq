package com.knowledgespike;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumeUserRegistration {

    private Channel channel;


    public ConsumeUserRegistration(Channel channel) {
        this.channel = channel;
    }

    public void consumeMessage(DeliverCallback callback) {
        try {
            channel.basicConsume("users", true, callback, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
