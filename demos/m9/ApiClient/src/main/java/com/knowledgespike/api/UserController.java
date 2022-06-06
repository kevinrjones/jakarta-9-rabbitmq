package com.knowledgespike.api;

import com.knowledgespike.quotes.messages.UserRegistrationMessage;
import com.knowledgespike.quotes.producer.PublishRegisterUser;
import com.knowledgespike.quotes.producer.PublishRegisterUserWithExchange;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController {

    private static final Logger logger
            = LoggerFactory.getLogger(UserController.class);
    public static Handler registerUser = ctx -> {

        PublishRegisterUser publishRegisterUser = new PublishRegisterUser(Application.connection);

        UserRegistrationMessage message;
        message = getHttpPostBody(ctx);

        if(message == null) return;

        if (publishRegisterUser.sendMessage(message)) {
            ctx.json(new Envelope<>());
            ctx.res.setStatus(200);
        } else {
            ctx.json(new Envelope<>(null, "Unable to send the message"));
            ctx.res.setStatus(400);
        }
    };

    public static Handler registerUserWithExchange = ctx -> {

        PublishRegisterUserWithExchange publishRegisterUser = new PublishRegisterUserWithExchange(Application.connection);

        UserRegistrationMessage message;
        message = getHttpPostBody(ctx);

        if(message == null) return;

        if (publishRegisterUser.sendMessage(message)) {
            ctx.json(new Envelope<>());
            ctx.res.setStatus(200);
        } else {
            ctx.json(new Envelope<>(null, "Unable to publish the message"));
            ctx.res.setStatus(400);
        }
    };

    @Nullable
    private static UserRegistrationMessage getHttpPostBody(Context ctx) {
        UserRegistrationMessage message;
        try {
            message = ctx.bodyAsClass(UserRegistrationMessage.class);
        } catch (Exception e) {
            logger.error("Unable to parse incoming JSON", e);
            ctx.json(new Envelope<>(null, "Unable to parse incoming JSON"));
            ctx.res.setStatus(400);
            message = null;
        }
        return message;
    }

}