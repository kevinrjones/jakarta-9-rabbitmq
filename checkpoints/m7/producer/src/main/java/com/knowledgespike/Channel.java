package com.knowledgespike;

import com.rabbitmq.client.BuiltinExchangeType;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface Channel {
    void basicPublish(String exchange, String queue, Object properties, byte[] message) throws IOException, TimeoutException;

    void exchangeDeclare(String exchangeName, BuiltinExchangeType exchangeType) throws IOException;
}
