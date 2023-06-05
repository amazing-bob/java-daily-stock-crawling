package com.example.javadailystockcrawling.service;

import com.example.javadailystockcrawling.dto.Stock;
import com.example.javadailystockcrawling.dto.UpperLimitStocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CrawlingService.class)
class CrawlingServiceTest {

    @Autowired
    MockMvc mockMvc;

    CrawlingService crawlingService;

    @BeforeEach
    void setUp() {
        crawlingService = new CrawlingService();
    }

    @Test
    void crawlingUppserLimitStocks() {
        UpperLimitStocks upperLimitStocks = crawlingService.crawlingUppserLimitStocks();
        List<Stock> kospiUpperLimitStocks = upperLimitStocks.getKospiUpperLimitStocks();
        List<Stock> kosdaqUpperLimitStocks = upperLimitStocks.getKosdaqUpperLimitStocks();
        System.out.println("kospiUpperLimitStocks = " + kospiUpperLimitStocks);
        System.out.println("-------------------------");
        System.out.println("kosdaqUpperLimitStocks = " + kosdaqUpperLimitStocks);

        String s ="-29.93%";
        double d = Double.parseDouble(s.replaceAll("%", "").strip());
        System.out.println(d);

    }

    @Test
    void crawlingMarketIndexes() {
    }
}