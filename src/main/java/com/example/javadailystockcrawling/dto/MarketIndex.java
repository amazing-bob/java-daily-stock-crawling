package com.example.javadailystockcrawling.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity
@Table(name = "market_index")
public class MarketIndex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private double ivalue;
    private double updown;
    private double rate;


    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###.##");
        return "%s\t\t%s  |  %s  |  %2.2f%%".formatted(this.name, df.format(this.ivalue), df.format(this.updown), this.rate);
    }
}
