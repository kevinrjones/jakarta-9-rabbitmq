package com.knowledgespike;

import com.rabbitmq.client.BuiltinExchangeType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProducerTest {

    ConsumeUserRegistrationWithRoutingKey producer;

    @Test
    public void testPublishMessage() throws IllegalAccessException {
        TestChannel channel = new TestChannel();

        ConsumeUserRegistrationWithRoutingKey publishRegisterUser = new ConsumeUserRegistrationWithRoutingKey(channel,
                "direct-exchange", "direct-queue", "direct-route");

        assertThat(channel.wasCalledCorrectly)
                .withFailMessage("==> Did you setup the exchange and the queue with the correct parameters?")
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

    @Override
    public void queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Object arguments) {
        wasCalledCorrectly = wasCalledCorrectly && (queue.equals("direct-queue") && durable && !exclusive && autoDelete && arguments == null);
    }

    @Override
    public void queueBind(String queue, String exchange, String routingKey, Object arguments) {
        wasCalledCorrectly = wasCalledCorrectly && (queue.equals("direct-queue")
                && exchange.equals("direct-exchange")
                && routingKey.equals("direct-route")
                && arguments == null);
    }
}
