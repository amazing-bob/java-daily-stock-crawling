package com.edgar.javadailystockcrawling.dto;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;




@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_price_info")
public class StockPriceInfo {
    public static String  KIND_UPPER_LIMIT = "UPPER_LIMIT";
    public static String  KIND_TRADING_VOLUME = "TRADING_VOLUME";
    public static String MARKET_KOSPI = "KOSPI";
    public static String MARKET_KOSDAQ = "KOSDAQ";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate toDate;
    private String kind;
    private String code;
    private String name;
    private String market;
    private long price;
    private long updown;
    private double rate;
    private long volume;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###");
        return "%s(%s)\n%s  |  %s  |  %2.2f%%  |  %s \n\n".formatted(this.name, this.code, df.format(this.price), df.format(this.updown), this.rate, df.format(this.volume));
    }
}
