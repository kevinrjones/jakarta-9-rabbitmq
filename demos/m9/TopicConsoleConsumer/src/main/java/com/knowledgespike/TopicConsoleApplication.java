package com.knowledgespike;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.QuoteException;
import com.knowledgespike.quotes.consumer.ConsumeQuotesMessage;
import com.knowledgespike.quotes.messages.CarDetailsMessage;
import com.knowledgespike.quotes.messages.HomeDetailsMessage;
import com.knowledgespike.quotes.messages.PriceQuoteMessage;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class TopicConsoleApplication {

    private static final Logger logger = LoggerFactory.getLogger(TopicConsoleApplication.class);

    public final static String EXCHANGE_NAME = "quotes_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {


        ExecutorService executor = Executors.newFixedThreadPool(10);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection("app:api:client");

        Runnable consumeCar = () -> {

            try {
                ConsumeQuotesMessage consumeLoanApplication = new ConsumeQuotesMessage(connection, EXCHANGE_NAME, BuiltinExchangeType.TOPIC, "quotes.car");

                boolean autoAcknowledge = false;

                System.out.println(" [*] Waiting for car messages. To exit press CTRL+C");

                CancelCallback cancelCallback = (consumerTag) -> System.out.println(" [x] Queue has been cancelled");

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                    PriceQuoteMessage response = null;

                    try {
                        String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        System.out.println(" [x] Received '" + message + "' with tag '" + consumerTag + "'");
                        ObjectMapper mapper = new ObjectMapper();

                        CarDetailsMessage carDetailsMessage = mapper.readValue(message, CarDetailsMessage.class);

                        System.out.println(" [x] Car quote message: " + carDetailsMessage);

                        response = new PriceQuoteMessage("", 23);


                    } catch (RuntimeException e) {
                        System.out.println(" [.] " + e);
                    } finally {
                        if (!consumeLoanApplication.reply(response, delivery.getProperties().getReplyTo(), delivery.getProperties().getCorrelationId(), delivery.getEnvelope().getDeliveryTag())) {
                            System.out.println("Unable to respond to the server");
                        }
                    }
                };

                consumeLoanApplication.consumeMessage(deliverCallback, cancelCallback, autoAcknowledge);
            } catch (IOException | QuoteException e) {
                logger.error("Failed to process message", e);
            }
        };

        try {
            ConsumeQuotesMessage consumeLoanApplication = new ConsumeQuotesMessage(connection, EXCHANGE_NAME, BuiltinExchangeType.TOPIC, "quotes.home");

            boolean autoAcknowledge = false;

            System.out.println(" [*] Waiting for home messages. To exit press CTRL+C");

            CancelCallback cancelCallback = (consumerTag) -> System.out.println(" [x] Queue has been cancelled");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {

                PriceQuoteMessage response = null;

                try {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Received '" + message + "' with tag '" + consumerTag + "'");
                    ObjectMapper mapper = new ObjectMapper();

                    HomeDetailsMessage homeDetailsMessage = mapper.readValue(message, HomeDetailsMessage.class);

                    System.out.println(" [x] Home quote message: " + homeDetailsMessage);

                    response = new PriceQuoteMessage("", 23);


                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e);
                } finally {
                    if (!consumeLoanApplication.reply(response, delivery.getProperties().getReplyTo(), delivery.getProperties().getCorrelationId(), delivery.getEnvelope().getDeliveryTag())) {
                        System.out.println("Unable to respond to the server");
                    }
                }
            };

            consumeLoanApplication.consumeMessage(deliverCallback, cancelCallback, autoAcknowledge);
        } catch (IOException | QuoteException e) {
            logger.error("Failed to process message", e);
        }

        try {
            ConsumeQuotesMessage consumeLoanApplication = new ConsumeQuotesMessage(connection, EXCHANGE_NAME, BuiltinExchangeType.TOPIC, "quotes.*");

            boolean autoAcknowledge = false;

            System.out.println(" [*] Waiting for all messages. To exit press CTRL+C");

            CancelCallback cancelCallback = (consumerTag) -> System.out.println(" [x] Queue has been cancelled");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                try {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    System.out.println(" [x] Received - ALL '" + message + "' with tag '" + consumerTag + "'");

                } catch (RuntimeException e) {
                    System.out.println(" [.] " + e);
                }
            };

            consumeLoanApplication.consumeMessage(deliverCallback, cancelCallback, autoAcknowledge);
        } catch (IOException | QuoteException e) {
            logger.error("Failed to process message", e);
        }

        executor.execute(consumeCar);
        executor.execute(consumeCar);
        executor.execute(consumeCar);
        executor.execute(consumeCar);

    }
}
