package com.example.javadailystockcrawling.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private String code;
    private String name;
    private long price;
    private long updown;
    private double rate;
    private long volume;

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###");
        return "%s(%s)\n%s  |  %s  |  %s%  |  %s \n\n".formatted(this.name, this.code, df.format(this.price), df.format(this.updown), this.rate, df.format(this.volume));
    }
}
