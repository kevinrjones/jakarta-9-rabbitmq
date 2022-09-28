package com.knowledgespike;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class TopicRoutingPriceQuotes {

    private Channel channel;


    public TopicRoutingPriceQuotes(Channel channel) {
        this.channel = channel;
    }

    public boolean sendMessage(String message) {
        try {
            channel.basicPublish("topic-exchange", "routing-key", null, message.getBytes());
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
