package com.example.javadailystockcrawling.dto;


public class Stock {
    private String name;
    private String price;
    private String updown;
    private String rate;
    private String volume;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpdown() {
        return updown;
    }

    public void setUpdown(String updown) {
        this.updown = updown;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "%s\n%s  |  %s  |  %s  |  %s \n".formatted(getName(), getPrice(), getUpdown(), getRate(), getVolume());
    }
}
