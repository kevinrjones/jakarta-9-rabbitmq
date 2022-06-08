package con.knowledgespike;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import org.assertj.core.api.Assertions;

import com.knowledgespike.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsumerTest {

    private Consumer consumer;

    @BeforeEach
    public void setUp() throws IOException, TimeoutException {
        consumer = new Consumer("localhost", "app:test");
    }

    @Test
    public void testConstructor() throws IllegalAccessException {
        Field[] fields = Consumer.class.getDeclaredFields();
        assertThat(fields.length)
                .withFailMessage("==> Have you created a `private Connection` field in the `Consumer` class.")
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
                .withFailMessage("==> Have you created a `private Connection` field in the `Consumer` class.")
                .isNotNull();

        Connection connection = (Connection) connectionField.get(consumer);

        assertThat(connection)
                .withFailMessage("==> Have you set the connection field in the `Consumer` class.")
                .isNotNull();
    }

    @Test
    public void testChannelCreation() throws IllegalAccessException {
        Field[] fields = Consumer.class.getDeclaredFields();
        Assertions.assertThat(fields.length)
                .withFailMessage("==> Have you created a `private Channel` field in the `Consumer` class.")
                .isGreaterThan(1);


        Field channelField = null;
        for (var field : fields) {
            if (field.getType() == Channel.class) {
                channelField = field;
                break;
            }
        }

        assertThat(channelField)
                .withFailMessage("==> Have you created a `private Channel` field in the `Consumer` class.")
                .isNotNull();
        channelField.setAccessible(true);

        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Consumer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(consumer);
        } catch (Throwable e) { }

        assertThat(channelInstanceMethod)
                .withFailMessage("==> Have you created a `createChannel` method in the `Consumer` class.")
                .isNotNull();

        Channel channel = (Channel) channelField.get(consumer);

        assertThat(channel)
                .withFailMessage("==> Have you created a channel in the `Consumer` class.")
                .isNotNull();

    }

    @Test
    public void testDeclareExchange() throws IllegalAccessException {


        Method declareExchangeInstanceMethod = null;
        try {
            declareExchangeInstanceMethod = Consumer.class.getDeclaredMethod("declareExchange");
        } catch (Throwable e) {
        }
        assertThat(declareExchangeInstanceMethod)
                .withFailMessage("==> Have you created a `declareExchange` method in the `Consumer` class.")
                .isNotNull();

        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Consumer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(consumer);
        } catch (Throwable e) { }


        try {
            declareExchangeInstanceMethod.setAccessible(true);
            declareExchangeInstanceMethod.invoke(consumer);
        } catch (Throwable e) {
            assertThat(e)
                    .withFailMessage("==> Unable to declare the exchange")
                    .isNull();
        }

    }
    
    @Test
    public void testQueueCreation() throws IllegalAccessException {

        Method exchangeInstanceMethod = null;
        try {
            exchangeInstanceMethod = Consumer.class.getDeclaredMethod("declareExchange");
            exchangeInstanceMethod.setAccessible(true);
            exchangeInstanceMethod.invoke(consumer);
        } catch (Throwable e) { }

        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Consumer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(consumer);
        } catch (Throwable e) { }

        Method queueInstanceMethod = null;
        try {
            queueInstanceMethod = Consumer.class.getDeclaredMethod("declareQueue");
            queueInstanceMethod.setAccessible(true);
            queueInstanceMethod.invoke(consumer);
        } catch (Throwable e) { }

        assertThat(queueInstanceMethod)
                .withFailMessage("==> Have you created a `declareQueue` method in the `Consumer` class.")
                .isNotNull();




        Field[] fields = Consumer.class.getDeclaredFields();

        Field queueNameField = null;
        for (var field : fields) {
            if (field.getType() == String.class && field.getName().equals("queueName")) {
                queueNameField = field;
                break;
            }
        }
        queueNameField.setAccessible(true);

        assertThat(queueNameField)
                .withFailMessage("==> Have you created a `private String queueName` field in the `Consumer` class.")
                .isNotNull();

        String queueName = (String) queueNameField.get(consumer);

        assertThat(queueName)
                .withFailMessage("==> Have you set the queueName field in the `Consumer` class.")
                .isNotNull();


    }

    @Test
    public void testBasicConsumeCreated() {

        Field[] fields = Consumer.class.getDeclaredFields();

        Field deliverCallbackField = null;
        for (var field : fields) {
            if (field.getType() == DeliverCallback.class) {
                deliverCallbackField = field;
                break;
            }
        }

        assertThat(deliverCallbackField)
                .withFailMessage("==> Have you created a `deliverCallbackField` field in the `Consumer` class.")
                .isNotNull();

        Method channelInstanceMethod = null;
        try {
            channelInstanceMethod = Consumer.class.getDeclaredMethod("createChannel");
            channelInstanceMethod.setAccessible(true);
            channelInstanceMethod.invoke(consumer);
        } catch (Throwable e) { }

        Method queueInstanceMethod = null;
        try {
            queueInstanceMethod = Consumer.class.getDeclaredMethod("declareQueue");
            queueInstanceMethod.setAccessible(true);
            queueInstanceMethod.invoke(consumer);
        } catch (Throwable e) { }

        Method consumeMessageInstanceMethod = null;
        try {
            consumeMessageInstanceMethod = Consumer.class.getDeclaredMethod("consumeMessage");
        } catch (Throwable e) { }

        assertThat(consumeMessageInstanceMethod)
                .withFailMessage("==> Have you created a `consumeMessage` method in the `Consumer` class.")
                .isNotNull();

    }

}
