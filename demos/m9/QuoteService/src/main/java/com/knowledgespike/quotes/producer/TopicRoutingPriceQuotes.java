package com.knowledgespike.quotes.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.messages.InsuranceMessage;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TopicRoutingPriceQuotes {
    private static final Logger logger
            = LoggerFactory.getLogger(TopicRoutingPriceQuotes.class);

    private final static String EXCHANGE_NAME = "quotes_exchange_topic";

    private final static boolean durable = false;
    private final static boolean autoDelete = false;
    private final static boolean exclusive = false;
    private final static Map<String, Object> arguments = null;
    private Connection connection;

    public TopicRoutingPriceQuotes(Connection connection) {
        this.connection = connection;
    }

    public String sendMessage(InsuranceMessage carDetailsMessage, String routingKey) {

        try (Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            var replyQueueName = channel.queueDeclare().getQueue();
            final String corrId = UUID.randomUUID().toString();
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            String carDetailsJson = mapper.writeValueAsString(carDetailsMessage);

            channel.basicPublish(EXCHANGE_NAME, routingKey, properties, carDetailsJson.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Sent '" + carDetailsJson + "'");

            final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
            new Thread(() -> {
                try {
                    Thread.sleep(5 * 1000);
                    response.offer("{\"state\": \"Done\"}");
                } catch (InterruptedException e) {
                    response.offer("{\"state\": \"Error\"}");
                }
            }).start();


            channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    logger.info(routingKey + " Quote Response: " + new String(delivery.getBody(), StandardCharsets.UTF_8));
                }
            }, consumerTag -> {
            });
            return response.take();
        } catch (Exception e) {
            logger.error("Unable to publish message", e);
            return null;
        }
    }
}
