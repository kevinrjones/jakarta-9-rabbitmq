package com.knowledgespike.api;

public class Envelope<T> {

    Envelope() {

    }

    public T Result;

    Envelope(T result, String errorMessage) {
        Result = result;
        ErrorMessage = errorMessage;
    }

    public String ErrorMessage;
    public String Body;
}
