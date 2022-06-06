package com.knowledgespike.quotes.messages;

public class LoanResponseMessage {
    public boolean ok;

    public LoanResponseMessage() {
    }

    public LoanResponseMessage(boolean ok) {
        this.ok = ok;
    }

    @Override
    public String toString() {
        return "LoanResponseMessage{" +
                "ok=" + ok +
                '}';
    }
}
