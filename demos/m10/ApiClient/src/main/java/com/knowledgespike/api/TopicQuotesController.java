package com.knowledgespike.api;

import com.knowledgespike.quotes.messages.CarDetailsMessage;
import com.knowledgespike.quotes.messages.HomeDetailsMessage;
import com.knowledgespike.quotes.producer.TopicRoutingPriceQuotes;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TopicQuotesController {
    private static final Logger logger
            = LoggerFactory.getLogger(TopicQuotesController.class);

    public static Handler getCarQuotes = ctx -> {
        var getPriceQuotes = new TopicRoutingPriceQuotes(Application.connection);

        CarDetailsMessage message = getCarDetailsMessage(ctx);

        if(message == null) return;

        var messageResponse = getPriceQuotes.sendMessage(message, "quotes.car");


        if (messageResponse != null) {
            ctx.json(messageResponse);
            ctx.res.setStatus(200);
        } else {
            ctx.json(new Envelope<>(null, "Unable to send the message"));
            ctx.res.setStatus(400);
        }
    };

    public static Handler getHomeQuotes = ctx -> {
        var getPriceQuotes = new TopicRoutingPriceQuotes(Application.connection);

        HomeDetailsMessage message = getHomeDetailsMessage(ctx);

        if(message == null) return;

        var messageResponse = getPriceQuotes.sendMessage(message, "quotes.home");

        if (messageResponse != null) {
            ctx.json(messageResponse);
            ctx.res.setStatus(200);
        } else {
            ctx.json(new Envelope<>(null, "Unable to send the message"));
            ctx.res.setStatus(400);
        }

    };

    @Nullable
    private static HomeDetailsMessage getHomeDetailsMessage(Context ctx) {
        HomeDetailsMessage message;
        try {
            message = ctx.bodyAsClass(HomeDetailsMessage.class);
        } catch (Exception e) {
            logger.error("Unable to parse incoming JSON", e);
            ctx.json(new Envelope<>(null, "Unable to parse incoming JSON"));
            ctx.res.setStatus(400);
            message = null;
        }
        return message;
    }

    @Nullable
    private static CarDetailsMessage getCarDetailsMessage(Context ctx) {
        CarDetailsMessage message;
        try {
            message = ctx.bodyAsClass(CarDetailsMessage.class);
        } catch (Exception e) {
            logger.error("Unable to parse incoming JSON", e);
            ctx.json(new Envelope<>(null, "Unable to parse incoming JSON"));
            ctx.res.setStatus(400);
            message = null;
        }
        return message;
    }
}
