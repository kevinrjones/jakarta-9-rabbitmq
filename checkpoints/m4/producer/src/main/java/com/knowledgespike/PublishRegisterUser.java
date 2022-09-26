package com.knowledgespike;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class PublishRegisterUser {

    private LocalChannel channel;


    public PublishRegisterUser(LocalChannel channel) {
        this.channel = channel;
    }

    public boolean sendMessage(String message) {
        try {
            channel.basicPublish("", "user", null, message.getBytes());
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
