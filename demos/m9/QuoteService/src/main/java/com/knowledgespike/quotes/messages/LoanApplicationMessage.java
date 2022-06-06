package com.knowledgespike.quotes.messages;

public class LoanApplicationMessage {

    public  String name;
    public String whatFor;
    public  double amount;

    public LoanApplicationMessage() {
    }

    public LoanApplicationMessage(String name, String whatFor, double amount) {
        this.name = name;
        this.whatFor = whatFor;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "LoanApplicationMessage{" +
                "name='" + name + '\'' +
                ", whatFor='" + whatFor + '\'' +
                ", amount=" + amount +
                '}';
    }
}
