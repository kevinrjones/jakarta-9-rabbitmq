package com.knowledgespike;

import com.rabbitmq.client.BuiltinExchangeType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProducerTest {

    PublishRegisterUserWithExchange producer;

    @Test
    public void testPublishMessage() throws IllegalAccessException {
        TestChannel channel = new TestChannel();

        PublishRegisterUserWithExchange publishRegisterUser = new PublishRegisterUserWithExchange(channel);

        publishRegisterUser.sendMessage("Hello World");

        assertThat(channel.wasCalledCorrectly)
                .withFailMessage("==> Did you declare the exchange with the correct parameters?")
                .isTrue();
    }

}

class TestChannel implements Channel {

    Boolean wasCalledCorrectly = false;

    @Override
    public void basicPublish(String exchange, String queue, Object properties, byte[] message) {
    }

    @Override
    public void exchangeDeclare(String exchangeName, BuiltinExchangeType exchangeType) {
        wasCalledCorrectly = exchangeName.equals("direct-exchange") && exchangeType == BuiltinExchangeType.DIRECT;
    }
}
