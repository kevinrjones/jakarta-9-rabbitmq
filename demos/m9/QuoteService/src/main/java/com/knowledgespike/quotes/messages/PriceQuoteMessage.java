package com.knowledgespike.quotes.messages;

public class PriceQuoteMessage {
    String companyName;
    int price;


    public PriceQuoteMessage() {
    }

    public PriceQuoteMessage(String companyName, int price) {

        this.companyName = companyName;
        this.price = price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "PriceQuoteMessage{" +
                "companyName='" + companyName + '\'' +
                ", price=" + price +
                '}';
    }
}
