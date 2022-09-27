package com.knowledgespike;

import com.rabbitmq.client.AMQP;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HeaderRoutingPriceQuotesTest {

    HeaderRoutingPriceQuotes producer;

    @Test
    public void testPublishMessage() throws IllegalAccessException {
        TestChannel channel = new TestChannel();

        HeaderRoutingPriceQuotes publishRegisterUser = new HeaderRoutingPriceQuotes(channel);
        var headers = new HashMap<String, Object>();
        headers.put("insurance", "car");

        publishRegisterUser.sendMessage("Hello World", headers);

        assertThat(channel.wasCalledCorrectly)
                .withFailMessage("==> Did you publish the message with the correct parameters?")
                .isTrue();
    }

}

class TestChannel implements Channel {

    Boolean wasCalledCorrectly = false;

    @Override
    public void basicPublish(String exchange, String queue, AMQP.BasicProperties properties, byte[] message) {
        String incomingMessage = new String(message);
        if (exchange.equals("headers-exchange") &&
                queue.equals("") &&
                properties != null &&
                incomingMessage.equals("Hello World"))
            wasCalledCorrectly = true;


        Object value = null;
        var headers = properties.getHeaders();
        if (headers != null)
            value = headers.get("insurance");

        wasCalledCorrectly = wasCalledCorrectly
                && headers != null
                && value != null
                && value.toString().equals("car");
    }
}
