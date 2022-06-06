package com.knowledgespike.api;

import com.knowledgespike.quotes.QuoteException;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import io.javalin.Javalin;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Application {
    static Connection connection;

    public static void main(String[] args) {
        Javalin app = Javalin.create()
                .start(7777);

        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            connection = factory.newConnection("app:api:rest");

            app.post("/users", UserController.registerUser);
            app.post("/loan", LoanController.apply);
        } catch (Exception e) {
            System.out.println("Unable to start connection to RabbitMQ");
            System.out.println(e.getCause().getMessage());
            app.stop();
        }
    }
}



