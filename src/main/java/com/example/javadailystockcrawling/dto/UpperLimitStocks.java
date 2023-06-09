package com.example.javadailystockcrawling.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.boot.jackson.JsonComponent;

import java.util.List;

@JsonComponent
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpperLimitStocks {
    private List<StockPriceInfo> kospiUpperLimitStockPriceInfos;
    private List<StockPriceInfo> kosdaqUpperLimitStockPriceInfos;

    public UpperLimitStocks() {    }

    public UpperLimitStocks(List<StockPriceInfo> kospiUpperLimitStockPriceInfos, List<StockPriceInfo> kosdaqUpperLimitStockPriceInfos) {
        this.kospiUpperLimitStockPriceInfos = kospiUpperLimitStockPriceInfos;
        this.kosdaqUpperLimitStockPriceInfos = kosdaqUpperLimitStockPriceInfos;
    }

    public List<StockPriceInfo> getKospiUpperLimitStocks() {
        return kospiUpperLimitStockPriceInfos;
    }

    public void setKospiUpperLimitStocks(List<StockPriceInfo> kospiUpperLimitStockPriceInfos) {
        this.kospiUpperLimitStockPriceInfos = kospiUpperLimitStockPriceInfos;
    }

    public List<StockPriceInfo> getKosdaqUpperLimitStocks() {
        return kosdaqUpperLimitStockPriceInfos;
    }

    public void setKosdaqUpperLimitStocks(List<StockPriceInfo> kosdaqUpperLimitStockPriceInfos) {
        this.kosdaqUpperLimitStockPriceInfos = kosdaqUpperLimitStockPriceInfos;
    }

    @Override
    public String toString() {
        return "UpperLimitStocks{" +
                "kospiUpperLimitStocks=" + kospiUpperLimitStockPriceInfos +
                ", kosdaqUpperLimitStocks=" + kosdaqUpperLimitStockPriceInfos +
                '}';
    }
}
