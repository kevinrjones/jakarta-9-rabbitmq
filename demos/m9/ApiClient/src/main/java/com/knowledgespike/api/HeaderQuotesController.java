package com.knowledgespike.api;

import com.knowledgespike.quotes.messages.CarDetailsMessage;
import com.knowledgespike.quotes.messages.HomeDetailsMessage;
import com.knowledgespike.quotes.producer.HeaderRoutingPriceQuotes;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class HeaderQuotesController {
    private static final Logger logger
            = LoggerFactory.getLogger(HeaderQuotesController.class);

    public static Handler getCarQuotes = ctx -> {
        var getPriceQuotes = new HeaderRoutingPriceQuotes(Application.connection);

        CarDetailsMessage message = getCarDetailsMessage(ctx);
        if (message == null) return;

        var headers = new HashMap<String, Object>();

        var vehicleType = ctx.queryParam("vehicle");
        if (vehicleType != null && !vehicleType.isEmpty())
            headers.put("vehicle", vehicleType);
        else
            headers.put("insurance", "car");


        var messageResponse = getPriceQuotes.sendMessage(message, headers);

        if (messageResponse != null) {
            ctx.json(messageResponse);
            ctx.res.setStatus(200);
        } else {
            ctx.json(new Envelope<>(null, "Unable to send the message"));
            ctx.res.setStatus(400);
        }

    };

    public static Handler getHomeQuotes = ctx -> {
        var getPriceQuotes = new HeaderRoutingPriceQuotes(Application.connection);

        HomeDetailsMessage message;
        message = getHomeDetailsMessage(ctx);

        if (message == null) return;

        var headers = new HashMap<String, Object>();

        headers.put("insurance", "buildings");
        headers.put("type", "business");

        var messageResponse = getPriceQuotes.sendMessage(message, headers);

        if (messageResponse != null) {
            ctx.json(messageResponse);
            ctx.res.setStatus(200);
        } else {
            ctx.json(new Envelope<>(null, "Unable to send the message"));
            ctx.res.setStatus(400);
        }

    };

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
}
