package com.knowledgespike.api;

import com.knowledgespike.quotes.messages.LoanApplicationMessage;
import com.knowledgespike.quotes.producer.PublishLoanApplication;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoanController {
    private static final Logger logger
            = LoggerFactory.getLogger(LoanController.class);

    public static Handler apply = ctx -> {


        // get the data from the incoming request
        LoanApplicationMessage message;
        message = getLoanApplicationMessage(ctx);

        if(message == null) return;

        var publishLoanApplication = new PublishLoanApplication(Application.connection);

        var messageResponse = publishLoanApplication.sendMessage(message);

        if(messageResponse != null) {
            ctx.json(new Envelope<>(messageResponse, null));
            ctx.res.setStatus(200);
        } else {
            ctx.json(new Envelope<>(null, "Unable to send the message"));
            ctx.res.setStatus(400);
        }

    };

    @Nullable
    private static LoanApplicationMessage getLoanApplicationMessage(Context ctx) {
        LoanApplicationMessage message;
        try {
            message = ctx.bodyAsClass(LoanApplicationMessage.class);
        } catch (Exception e) {
            logger.error("Unable to parse incoming JSON", e);
            ctx.json(new Envelope<>(null, "Unable to parse incoming JSON"));
            ctx.res.setStatus(400);
            message = null;
        }
        return message;
    }
}
