package com.knowledgespike;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProducerTest {

    Producer producer;

    @BeforeEach
    public void setUp() throws IOException, TimeoutException {
        producer = new Producer("localhost", "app:test");
    }

    @Test
    public void testConstructor() throws IllegalAccessException {
        Field[] fields = Producer.class.getDeclaredFields();
        assertThat(fields.length)
                .withFailMessage("==> Have you created a `private Connection` field in the `Producer` class.")
                .isGreaterThan(0);

        Field connectionField = null;
        for (var field : fields) {
            if (field.getType() == Connection.class) {
                connectionField = field;
                break;
            }
        }
        connectionField.setAccessible(true);

        assertThat(connectionField)
                .withFailMessage("==> Have you created a `private Connection` field in the `Producer` class.")
                .isNotNull();

        Connection connection = (Connection) connectionField.get(producer);

        assertThat(connectionField)
                .withFailMessage("==> Have you created a connection in the `Producer` class.")
                .isNotNull();

    }

    @Test
    public void testChannelCreation() throws IllegalAccessException {
        Field[] fields = Producer.class.getDeclaredFields();
        Assertions.assertThat(fields.length)
                .withFailMessage("==> Have you created a `private Channel` field in the `Producer` class.")
                .isGreaterThan(1);


        Field channelField = null;
        for (var field : fields) {
            if (field.getType() == Channel.class) {
                channelField = field;
                break;
            }
        }

        assertThat(channelField)
                .withFailMessage("==> Have you created a `private Channel` field in the `Producer` class.")
                .isNotNull();
        channelField.setAccessible(true);

        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Producer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(producer);
        } catch (Throwable e) { }

        assertThat(channelInstanceMethod)
                .withFailMessage("==> Have you created a `createChannel` method in the `Producer` class.")
                .isNotNull();

        Channel channel = (Channel) channelField.get(producer);

        assertThat(channel)
                .withFailMessage("==> Have you created a channel in the `Producer` class.")
                .isNotNull();

    }

    @Test
    public void testDeclareExchange() throws IllegalAccessException {

        Field[] fields = Producer.class.getDeclaredFields();

        Field nameField = null;
        for (var field : fields) {
            if (field.getType() == String.class && Modifier.isStatic(field.getModifiers())) {
                nameField = field;
                break;
            }
        }

        assertThat(nameField)
                .withFailMessage("==> Have you created a `private final static String EXCHANGE_NAME` field in the `Producer` class.")
                .isNotNull();

        nameField.setAccessible(true);

        var name = (String) nameField.get(null);
        assertThat(name)
                .withFailMessage("==> Have you created a `private final static String EXCHANGE_NAME` field with the value `direct-exchange` in the `Producer` class.")
                .isEqualTo("direct-exchange");
        nameField.setAccessible(true);

        Method declareExchangeInstanceMethod = null;
        try {
            declareExchangeInstanceMethod = Producer.class.getDeclaredMethod("declareExchange");
        } catch (Throwable e) {
        }
        assertThat(declareExchangeInstanceMethod)
                .withFailMessage("==> Have you created a `declareExchange` method in the `Producer` class.")
                .isNotNull();

        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Producer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(producer);
        } catch (Throwable e) { }


        try {
            declareExchangeInstanceMethod.setAccessible(true);
            declareExchangeInstanceMethod.invoke(producer);
        } catch (Throwable e) {
            assertThat(e)
                    .withFailMessage("==> Unable to declare the exchange")
                    .isNull();
        }

    }

    @Test
    public void testPublishMessage() throws IllegalAccessException {

        Method publishMessageInstanceMethod = null;
        try {
            publishMessageInstanceMethod = Producer.class.getDeclaredMethod("publishMessage", String.class, String.class);
        } catch (Throwable e) {
        }
        assertThat(publishMessageInstanceMethod)
                .withFailMessage("==> Have you created a `publishMessage` method in the `Producer` class.")
                .isNotNull();

        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Producer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(producer);
        } catch (Throwable e) { }


        try {
            publishMessageInstanceMethod.setAccessible(true);
            publishMessageInstanceMethod.invoke(producer, "Message", "direct-route");
        } catch (Throwable e) {
            assertThat(e)
                    .withFailMessage("==> Unable to publish a message")
                    .isNull();
        }

    }

}
