package com.knowledgespike;

import com.rabbitmq.client.AMQP;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class HeaderRoutingPriceQuotes {

    private Channel channel;


    public HeaderRoutingPriceQuotes(Channel channel) {
        this.channel = channel;
    }

    public boolean sendMessage(String message, Map<String, Object> headers) {
        try {
            AMQP.BasicProperties properties = null;
            channel.basicPublish("headers-exchange", "", properties, message.getBytes());
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
