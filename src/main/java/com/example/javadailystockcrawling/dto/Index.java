package com.example.javadailystockcrawling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Index {
    private String name;
    private String value;
    private String updown;
    private String rate;

    public Index() {
    }

    public Index(String name, String value, String updown, String rate) {
        this.name = name;
        this.value = value;
        this.updown = updown;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    @Override
    public String toString() {
        return "%s\t\t%s  |  %s  |  %s".formatted(getName(),getValue(), getUpdown(), getRate());
    }
}
