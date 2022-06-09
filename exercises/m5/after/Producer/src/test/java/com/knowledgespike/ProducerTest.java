package com.knowledgespike;

import com.rabbitmq.client.AMQP;
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

        assertThat(connection)
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
        } catch (Throwable e) {
        }

        assertThat(channelInstanceMethod)
                .withFailMessage("==> Have you created a `createChannel` method in the `Producer` class.")
                .isNotNull();

        Channel channel = (Channel) channelField.get(producer);

        assertThat(channel)
                .withFailMessage("==> Have you created a channel in the `Producer` class.")
                .isNotNull();

    }

    @Test
    public void testQueueCreation() throws IllegalAccessException {

        Field[] fields = Producer.class.getDeclaredFields();
        Assertions.assertThat(fields.length)
                .withFailMessage("==> Have you created a `QUEUE_NAME` field in the `Producer` class.")
                .isGreaterThan(1);


        Field nameField = null;
        for (var field : fields) {
            if (field.getType() == String.class && Modifier.isStatic(field.getModifiers())) {
                nameField = field;
                break;
            }
        }

        assertThat(nameField)
                .withFailMessage("==> Have you created a `private final static String QUEUE_NAME` field in the `Producer` class.")
                .isNotNull();

        nameField.setAccessible(true);

        var name = (String) nameField.get(null);
        assertThat(name)
                .withFailMessage("==> Have you created a `private final static String QUEUE_NAME` feld with the value `rpc` in the `Producer` class.")
                .isEqualTo("rpc");

        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Producer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(producer);
        } catch (Throwable e) {
        }

        Method queueInstanceMethod = null;
        try {
            queueInstanceMethod = Producer.class.getDeclaredMethod("declareQueue");
            queueInstanceMethod.setAccessible(true);
            queueInstanceMethod.invoke(producer);
        } catch (Throwable e) {
            assertThat(e)
                    .withFailMessage("==> Unable to declare a queue")
                    .isNull();
        }

        assertThat(queueInstanceMethod)
                .withFailMessage("==> Have you created a `declareQueue` method in the `Producer` class.")
                .isNotNull();

    }

    @Test
    public void testTemporaryQueueCreation() throws IllegalAccessException {

        // Does the 'replyTo' field exist?
        Field[] fields = Producer.class.getDeclaredFields();
        Field replyToField = null;
        for (var field : fields) {
            if (field.getType() == String.class && field.getName().equals("replyTo")) {
                replyToField = field;
                break;
            }
        }

        assertThat(replyToField)
                .withFailMessage("==> Have you created a `private replyToField` field in the `Producer` class.")
                .isNotNull();

        // Call 'createChannel' to get the channel for later in the test
        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Producer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(producer);
        } catch (Throwable e) {
        }

        // does the 'createTemporaryQueue' method exist
        Method temporaryQueueInstanceMethod = null;
        try {
            temporaryQueueInstanceMethod = Producer.class.getDeclaredMethod("createTemporaryQueue");
            temporaryQueueInstanceMethod.setAccessible(true);
            temporaryQueueInstanceMethod.invoke(producer);
        } catch (Throwable e) {
            assertThat(e)
                    .withFailMessage("==> Unable to create a temporary queue")
                    .isNull();
        }

        assertThat(temporaryQueueInstanceMethod)
                .withFailMessage("==> Have you created a `createTemporaryQueue` method in the `Producer` class.")
                .isNotNull();

        replyToField.setAccessible(true);
        var name = (String) replyToField.get(producer);
        assertThat(name)
                .withFailMessage("==> Have you created a `replyTo` field and set its value in the createTemporaryQueue method")
                .isNotEmpty();

    }

    @Test
    public void testCorrelationIdCreation() throws IllegalAccessException {

        // Check that the correlationId field exists
        Field[] fields = Producer.class.getDeclaredFields();
        Field correlationIdField = null;
        for (var field : fields) {
            if (field.getType() == String.class && field.getName().equals("correlationId")) {
                correlationIdField = field;
                break;
            }
        }

        assertThat(correlationIdField)
                .withFailMessage("==> Have you created a `private correlationId` field in the `Producer` class.")
                .isNotNull();

        // Check tha the 'createCorrelationId' method exists and call it
        Method createCorrelationIdInstanceMethod = null;
        try {
            createCorrelationIdInstanceMethod = Producer.class.getDeclaredMethod("createCorrelationId");
            createCorrelationIdInstanceMethod.setAccessible(true);
            createCorrelationIdInstanceMethod.invoke(producer);
        } catch (Throwable e) {
            assertThat(e)
                    .withFailMessage("==> Unable to create correlation id")
                    .isNull();
        }

        assertThat(createCorrelationIdInstanceMethod)
                .withFailMessage("==> Have you created a `createCorrelationId` method in the `Producer` class.")
                .isNotNull();

        // Check that the 'correlationId' field has been set
        correlationIdField.setAccessible(true);
        var name = (String) correlationIdField.get(producer);
        assertThat(name)
                .withFailMessage("==> Have you created a `correlationId` field and set its value in the createCorrelationId method")
                .isNotEmpty();

    }

    @Test
    public void testPropertiesCreation() throws IllegalAccessException {

        // check that the 'properties' field exists
        Field[] fields = Producer.class.getDeclaredFields();
        Field propertiesField = null;
        for (var field : fields) {
            if (field.getType() == AMQP.BasicProperties.class) {
                propertiesField = field;
                break;
            }
        }

        assertThat(propertiesField)
                .withFailMessage("==> Have you created a `private properties` field in the `Producer` class.")
                .isNotNull();


        // check that the 'buildProperties' method exists and call it
        Method buildPropertiesInstanceMethod = null;
        try {
            buildPropertiesInstanceMethod = Producer.class.getDeclaredMethod("buildProperties");
            buildPropertiesInstanceMethod.setAccessible(true);
            buildPropertiesInstanceMethod.invoke(producer);
        } catch (Throwable e) {
            assertThat(e)
                    .withFailMessage("==> Unable to build properties")
                    .isNull();
        }

        assertThat(buildPropertiesInstanceMethod)
                .withFailMessage("==> Have you created the `buildProperties` method in the `Producer` class.")
                .isNotNull();


        // check that the properties field is set
        propertiesField.setAccessible(true);
        var name = (AMQP.BasicProperties) propertiesField.get(producer);
        assertThat(name)
                .withFailMessage("==> Have you created a `properties` field and set its value in the buildProperties method")
                .isNotNull();

    }

    @Test
    public void testPublishMessage() throws IllegalAccessException {

        Field[] fields = Producer.class.getDeclaredFields();

        Field nameField = null;
        for (var field : fields) {
            if (field.getType() == String.class && Modifier.isStatic(field.getModifiers())) {
                nameField = field;
                break;
            }
        }

        assertThat(nameField)
                .withFailMessage("==> Have you created a `private final static String QUEUE_NAME` field in the `Producer` class.")
                .isNotNull();

        nameField.setAccessible(true);

        var name = (String) nameField.get(null);
        assertThat(name)
                .withFailMessage("==> Have you created a `private final static String QUEUE_NAME` feld with the value `rpc` in the `Producer` class.")
                .isEqualTo("rpc");
        nameField.setAccessible(true);

        Method publishMessageInstanceMethod = null;
        try {
            publishMessageInstanceMethod = Producer.class.getDeclaredMethod("publishMessage", String.class);
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
        } catch (Throwable e) {
        }


        try {
            publishMessageInstanceMethod.setAccessible(true);
            publishMessageInstanceMethod.invoke(producer, "Message");
        } catch (Throwable e) {
            assertThat(e)
                    .withFailMessage("==> Unable to publish a message")
                    .isNull();
        }

    }

    @Test
    public void testConsumeResponseCreated() {

        Method consumeMessageInstanceMethod = null;
        try {
            consumeMessageInstanceMethod =Producer.class.getDeclaredMethod("consumeResponse");
        } catch (Throwable e) {
        }

        assertThat(consumeMessageInstanceMethod)
                .withFailMessage("==> Have you created a `consumeMessage` method in the `Producer` class.")
                .isNotNull();

    }
}
