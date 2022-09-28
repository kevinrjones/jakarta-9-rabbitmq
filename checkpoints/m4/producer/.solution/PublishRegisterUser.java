package com.knowledgespike;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class PublishRegisterUser {

    private Channel channel;


    public PublishRegisterUser(Channel channel) {
        this.channel = channel;
    }

    public boolean sendMessage(String message) {
        try {
            channel.basicPublish("", "users", null, message.getBytes());
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
