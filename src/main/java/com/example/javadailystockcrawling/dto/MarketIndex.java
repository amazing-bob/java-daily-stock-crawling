package com.example.javadailystockcrawling.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDate toDate;
    private String name;
    private double ivalue;
    private double updown;
    private double rate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###.##");
        return "%s\t\t%s  |  %s  |  %2.2f%%".formatted(this.name, df.format(this.ivalue), df.format(this.updown), this.rate);
    }
}
