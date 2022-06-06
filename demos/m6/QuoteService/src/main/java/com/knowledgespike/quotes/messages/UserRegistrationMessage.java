package com.knowledgespike.quotes.messages;


public class UserRegistrationMessage {

    public UserRegistrationMessage() {

    }

    public UserRegistrationMessage(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String name;
    public String password;

    @Override
    public String toString() {
        return "UserRegistrationMessage{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
