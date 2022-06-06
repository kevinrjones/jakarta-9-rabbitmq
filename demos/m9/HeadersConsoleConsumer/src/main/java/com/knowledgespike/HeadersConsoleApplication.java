package com.knowledgespike;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgespike.quotes.consumer.HeaderQuotesMessage;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class HeadersConsoleApplication {

    private static final Logger logger
            = LoggerFactory.getLogger(HeadersConsoleApplication.class);

    public final static String EXCHANGE_NAME = "quotes_exchange_headers";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        var connection = factory.newConnection("app:api:client");

        try {

            Map<String, Object> headers = new HashMap<>();
            headers.put("x-match", "any");
            headers.put("insurance", "car");
            headers.put("vehicle", "motorbike");

            var headerQuotesMessage = new HeaderQuotesMessage(connection, EXCHANGE_NAME, BuiltinExchangeType.HEADERS, "quotes_car", headers);

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
                    if (!headerQuotesMessage.reply(response, delivery.getProperties().getReplyTo(), delivery.getProperties().getCorrelationId(), delivery.getEnvelope().getDeliveryTag())) {
                        System.out.println("Unable to respond to the server");
                    }
                }
            };

            headerQuotesMessage.consumeMessage(deliverCallback, cancelCallback, autoAcknowledge);
        } catch (IOException qe) {
            logger.error("Failed to process message", qe);
        }


        try {
            Map<String, Object> headers = new HashMap<>();
            headers.put("x-match", "all");
            headers.put("insurance", "buildings");
            headers.put("type", "home");


            var headerQuotesMessage = new HeaderQuotesMessage(connection, EXCHANGE_NAME, BuiltinExchangeType.HEADERS, "quotes_home", headers);

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
                    if (!headerQuotesMessage.reply(response, delivery.getProperties().getReplyTo(), delivery.getProperties().getCorrelationId(), delivery.getEnvelope().getDeliveryTag())) {
                        System.out.println("Unable to respond to the server");
                    }
                }
            };

            headerQuotesMessage.consumeMessage(deliverCallback, cancelCallback, autoAcknowledge);
        } catch (IOException qe) {
            logger.error("Failed to process message", qe);
        }

    }
}
