package com.knowledgespike.quotes;


import java.io.IOException;

public class QuoteException extends Exception {
    public QuoteException(String message) {
        super(message);
    }

    public QuoteException(String message, Exception e) {
        super(message, e);
    }
}
