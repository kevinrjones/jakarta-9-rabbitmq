package com.knowledgespike;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProducerTest {

    PublishRegisterUser producer;

    @Test
    public void testPublishMessage() throws IllegalAccessException {
        TestChannel channel = new TestChannel();

        PublishRegisterUser publishRegisterUser = new PublishRegisterUser(channel);

        publishRegisterUser.sendMessage("Hello World");

        assertThat(channel.wasCalledCorrectly)
                .withFailMessage("==> Did you publish the message with the correct parameters?")
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
}
