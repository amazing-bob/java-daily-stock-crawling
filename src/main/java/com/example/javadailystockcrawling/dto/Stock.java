package com.example.javadailystockcrawling.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.text.DecimalFormat;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String code;
    private String name;
    private String market;
    private long price;
    private long updown;
    private double rate;
    private long volume;

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###");
        return "%s(%s)\n%s  |  %s  |  %2.2f%%  |  %s \n\n".formatted(this.name, this.code, df.format(this.price), df.format(this.updown), this.rate, df.format(this.volume));
    }
}
