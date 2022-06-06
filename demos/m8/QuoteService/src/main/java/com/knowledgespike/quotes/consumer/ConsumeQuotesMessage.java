package com.knowledgespike.quotes.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.QuoteException;
import com.knowledgespike.quotes.messages.PriceQuoteMessage;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ConsumeQuotesMessage {
    private static final Logger logger
            = LoggerFactory.getLogger(ConsumeQuotesMessage.class);


    private Channel channel;
    private String queueName;

    private List<String> queueNames = new ArrayList<>();

    public ConsumeQuotesMessage(Connection connection, String exchangeName, BuiltinExchangeType exchangeType) throws QuoteException, IOException {
        channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, exchangeType);
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, "");
    }

    public ConsumeQuotesMessage(Connection connection, String exchangeName, BuiltinExchangeType exchangeType, String routingKey) throws QuoteException, IOException {
        channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, exchangeType);
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchangeName, routingKey);
    }


    public void consumeMessage(DeliverCallback callback, CancelCallback cancelCallback, boolean autoAcknowledge) {
        try {
            channel.basicConsume(queueName, autoAcknowledge, callback, cancelCallback);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println();
    }

    public boolean reply(PriceQuoteMessage message, String queueName, String correlationId, long deliveryTag) {
        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                .Builder()
                .correlationId(correlationId)
                .build();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String response = mapper.writeValueAsString(message);
            channel.basicPublish("", queueName, replyProps, response.getBytes(StandardCharsets.UTF_8));
            channel.basicAck(deliveryTag, false);
            return true;
        } catch (Exception e) {
            logger.error("Unable to publish message", e);
            return false;
        }
    }


}
