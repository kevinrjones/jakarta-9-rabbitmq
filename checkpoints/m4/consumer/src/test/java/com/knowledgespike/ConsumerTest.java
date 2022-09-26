package com.knowledgespike;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ConsumerTest {

    ConsumeUserRegistration consumer;

    @Test
    public void testConsumeMessage() throws IllegalAccessException {
        TestChannel channel = new TestChannel();

        ConsumeUserRegistration publishRegisterUser = new ConsumeUserRegistration(channel);

        publishRegisterUser.consumeMessage(new TestDeliverCallback());

        assertThat(channel.wasCalledCorrectly)
                .withFailMessage("==> Did you consume the message with the correct parameters?")
                .isTrue();
    }

}

class TestChannel implements Channel {

    Boolean wasCalledCorrectly = false;

    @Override
    public void basicPublish(String exchange, String queue, Object properties, byte[] message) {
        String incomingMessage = new String(message);
        if (exchange.equals("") &&
                queue.equals("users") &&
                properties == null &&
                incomingMessage.equals("Hello World"))
            wasCalledCorrectly = true;
    }

    @Override
    public void basicConsume(String queueName, boolean autoAck, DeliverCallback callback, CancelCallback cancelCallback) throws IOException {

        wasCalledCorrectly = queueName.equals("users") &&
                autoAck &&
                callback != null &&
                cancelCallback == null;
    }
}

class TestDeliverCallback implements DeliverCallback  {

}
