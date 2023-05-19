package com.example.javadailystockcrawling.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;

@JsonComponent
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpperLimitStocks {
    private List<Stock> kospiUpperLimitStocks;
    private List<Stock> kosdaqUpperLimitStocks;

    public UpperLimitStocks() {    }

    public UpperLimitStocks(List<Stock> kospiUpperLimitStocks, List<Stock> kosdaqUpperLimitStocks) {
        this.kospiUpperLimitStocks = kospiUpperLimitStocks;
        this.kosdaqUpperLimitStocks = kosdaqUpperLimitStocks;
    }

    public List<Stock> getKospiUpperLimitStocks() {
        return kospiUpperLimitStocks;
    }

    public void setKospiUpperLimitStocks(List<Stock> kospiUpperLimitStocks) {
        this.kospiUpperLimitStocks = kospiUpperLimitStocks;
    }

    public List<Stock> getKosdaqUpperLimitStocks() {
        return kosdaqUpperLimitStocks;
    }

    public void setKosdaqUpperLimitStocks(List<Stock> kosdaqUpperLimitStocks) {
        this.kosdaqUpperLimitStocks = kosdaqUpperLimitStocks;
    }

    @Override
    public String toString() {
        return "UpperLimitStocks{" +
                "kospiUpperLimitStocks=" + kospiUpperLimitStocks +
                ", kosdaqUpperLimitStocks=" + kosdaqUpperLimitStocks +
                '}';
    }
}
