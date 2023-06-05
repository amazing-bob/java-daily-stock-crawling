package com.example.javadailystockcrawling.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.text.DecimalFormat;

@JsonComponent()
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Index {
    private String name;
    private long value;
    private long updown;
    private double rate;


    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###,###");
        return "%s\t\t%s  |  %s  |  %s".formatted(this.name, df.format(this.value), df.format(this.updown), this.rate);
    }
}
