package com.knowledgespike.quotes.messages;

public class CarDetailsMessage extends InsuranceMessage {

    String registration;
    int mileage;

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String generateKey() {
        if(price > 30000) {
            return "car-high";
        }
        return "car";
    }

    @Override
    public String toString() {
        return "CarDetailsMessage{" +
                "price='" + price + '\'' +
                ", registration='" + registration + '\'' +
                ", mileage=" + mileage +
                '}';
    }
}
