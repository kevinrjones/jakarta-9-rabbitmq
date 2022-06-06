package com.knowledgespike.quotes.messages;

public class HomeDetailsMessage extends InsuranceMessage {


    String address;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "HomeDetailsMessage{" +
                "price='" + price + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String generateKey() {
        if(price > 300000) return "home-high";

        return "home";
    }
}
