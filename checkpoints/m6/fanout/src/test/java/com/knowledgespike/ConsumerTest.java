package com.knowledgespike;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ConsumerTest {

    ConsumeQuotesMessage consumer;

    @Test
    public void testConsumeMessage() throws IOException, TimeoutException {
        TestChannel channel = new TestChannel();

        ConsumeQuotesMessage consumer = new ConsumeQuotesMessage(channel, "TestExchange");


        assertThat(channel.wasCalledCorrectly)
                .withFailMessage("==> Did you create the properties with the correct correlation id?")
                .isTrue();
    }

}

class TestChannel implements Channel {

    Boolean wasCalledCorrectly = false;

    @Override
    public void basicPublish(String exchange, String queue, AMQP.BasicProperties properties, byte[] message) {
    }

    @Override
    public void basicConsume(String queueName, boolean autoAck, DeliverCallback callback, CancelCallback cancelCallback) throws IOException {

    }

    @Override
    public void basicAck(long deliveryTag, boolean multiple) {

    }

    @Override
    public void exchangeDeclare(String exchangeName, BuiltinExchangeType exchangeType) {
        wasCalledCorrectly = exchangeName.equals("TestExchange") && exchangeType == BuiltinExchangeType.FANOUT;
    }
}
