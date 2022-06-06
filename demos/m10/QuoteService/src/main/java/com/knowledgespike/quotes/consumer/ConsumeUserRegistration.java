package com.knowledgespike.quotes.consumer;

import com.knowledgespike.quotes.QuoteException;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumeUserRegistration {
    private static final Logger logger
            = LoggerFactory.getLogger(ConsumeUserRegistration.class);
    public final static String QUEUE_NAME = "users";

    Channel channel;

    public ConsumeUserRegistration(Connection connection) throws IOException {
        channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    }

    public void consumeMessage(DeliverCallback callback) throws QuoteException {
        try {
            channel.basicConsume(QUEUE_NAME, true, callback, (CancelCallback) null);
        } catch (Exception e) {
            throw new QuoteException("Unable to consume message", e);
        }
    }

    public boolean pollQueue(DeliverCallback callback) throws QuoteException {
        try {
            GetResponse response = channel.basicGet(QUEUE_NAME, true);
            if (response == null) {
                return false;
            } else {
                Delivery delivery = new Delivery(response.getEnvelope(), response.getProps(), response.getBody());
                callback.handle("", delivery);
                return true;
            }
        } catch (IOException e) {
            throw new QuoteException("Unable to poll queue", e);
        }
    }

    public void close() throws IOException, TimeoutException {
        channel.close();
    }
}


















