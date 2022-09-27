package com.knowledgespike;

import com.rabbitmq.client.AMQP;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ConsumerTest {

    Consumer consumer;

    @Test
    public void testConsumeMessage() throws IOException, TimeoutException {
        TestChannel channel = new TestChannel();

        Consumer consumer = new Consumer(channel);

        consumer.sendResponseMessage("replyTo", "correlationId", 23);

        assertThat(channel.wasCalledCorrectly)
                .withFailMessage("==> Did you create the properties with the correct correlation id?")
                .isTrue();
    }

}

class TestChannel implements Channel {

    Boolean wasCalledCorrectly = false;

    @Override
    public void basicPublish(String exchange, String queue, AMQP.BasicProperties properties, byte[] message) {
        wasCalledCorrectly = properties != null && properties.getCorrelationId().equals("correlationId");
    }

    @Override
    public void basicConsume(String queueName, boolean autoAck, DeliverCallback callback, CancelCallback cancelCallback) throws IOException {

    }

    @Override
    public void basicAck(long deliveryTag, boolean multiple) {

    }
}
