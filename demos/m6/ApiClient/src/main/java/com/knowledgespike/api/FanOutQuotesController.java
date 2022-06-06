package com.knowledgespike.api;

import com.knowledgespike.quotes.messages.CarDetailsMessage;
import com.knowledgespike.quotes.producer.PublishFanOutPriceQuotes;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FanOutQuotesController {
    private static final Logger logger
            = LoggerFactory.getLogger(FanOutQuotesController.class);

    public static Handler getQuotes = ctx -> {

        CarDetailsMessage message = getCarDetailsMessage(ctx);

        if(message == null) return;

        PublishFanOutPriceQuotes getPriceQuotes = new PublishFanOutPriceQuotes(Application.connection);

        var reply = getPriceQuotes.sendMessage(message);

        if (reply != null) {
            ctx.json(reply);
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

}
