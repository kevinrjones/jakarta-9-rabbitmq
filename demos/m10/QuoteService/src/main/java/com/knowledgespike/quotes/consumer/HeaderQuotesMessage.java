package com.knowledgespike.quotes.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.messages.PriceQuoteMessage;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HeaderQuotesMessage {
    private static final Logger logger
            = LoggerFactory.getLogger(HeaderQuotesMessage.class);


    String queueName;
    Channel channel;

    public HeaderQuotesMessage(Connection connection, String exchangeName, BuiltinExchangeType exchangeType, String queueName, Map<String, Object> headers) throws  IOException {
        this.queueName = queueName;
        channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, exchangeType);
        channel.queueDeclare(queueName, false, false, true, null);
        channel.queueBind(queueName, exchangeName, "", headers);
    }


    public void consumeMessage(DeliverCallback callback, CancelCallback cancelCallback, boolean autoAcknowledge) {
        try {
            channel.basicConsume(queueName, autoAcknowledge, callback, cancelCallback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    public boolean reply(PriceQuoteMessage message, String replyTo, String correlationId, long deliveryTag) {
        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .build();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(message);

            channel.basicPublish("", replyTo, replyProps, response.getBytes(StandardCharsets.UTF_8));
            channel.basicAck(deliveryTag, false);
            return true;
        } catch (Exception e) {
            logger.error("Unable to publish message", e);
            return false;
        }
    }
}
