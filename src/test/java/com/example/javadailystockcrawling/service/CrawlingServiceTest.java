package com.example.javadailystockcrawling.service;

import com.example.javadailystockcrawling.dto.StockPriceInfo;
import com.example.javadailystockcrawling.dto.UpperLimitStocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CrawlingService.class)
class CrawlingServiceTest {

    @Autowired
    MockMvc mockMvc;

    CrawlingService crawlingService;

    @BeforeEach
    void setUp() {
//        crawlingService = new CrawlingService();
    }

    @Test
    void crawlingUppserLimitStocks() {
        UpperLimitStocks upperLimitStocks = crawlingService.crawlingUppserLimitStocks();
        List<StockPriceInfo> kospiUpperLimitStockPriceInfos = upperLimitStocks.getKospiUpperLimitStocks();
        List<StockPriceInfo> kosdaqUpperLimitStockPriceInfos = upperLimitStocks.getKosdaqUpperLimitStocks();
        System.out.println("kospiUpperLimitStocks = " + kospiUpperLimitStockPriceInfos);
        System.out.println("-------------------------");
        System.out.println("kosdaqUpperLimitStocks = " + kosdaqUpperLimitStockPriceInfos);

        String s ="-29.93%";
        double d = Double.parseDouble(s.replaceAll("%", "").strip());
        System.out.println(d);

    }

    @Test
    void crawlingMarketIndexes() {
    }
}