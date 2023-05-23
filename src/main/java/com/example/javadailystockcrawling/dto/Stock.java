package com.example.javadailystockcrawling.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private String name;
    private String price;
    private String updown;
    private String rate;
    private String volume;

    @Override
    public String toString() {
        return "%s\n%s  |  %s  |  %s  |  %s \n\n".formatted(this.name, this.price, this.updown, this.rate, this.volume);
    }
}
