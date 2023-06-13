package com.edgar.javadailystockcrawling.service;

import com.edgar.javadailystockcrawling.dto.MarketIndex;
import com.edgar.javadailystockcrawling.dto.StockPriceInfo;
import com.edgar.javadailystockcrawling.dto.UpperLimitStocks;
import com.edgar.javadailystockcrawling.mapper.MarketIndexMapper;
import com.edgar.javadailystockcrawling.repository.MarketIndexRepository;
import com.edgar.javadailystockcrawling.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CrawlingService.class)
class CrawlingServiceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StockRepository stockRepository;

    @MockBean
    MarketIndexRepository  marketIndexRepository;

    CrawlingService crawlingService;

    @BeforeEach
    void setUp() {
        crawlingService = new CrawlingService(stockRepository, marketIndexRepository);
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
        List<MarketIndex> marketIndexList = crawlingService.crawlingMarketIndexes();
//        System.out.println("marketIndexList = " + marketIndexList);
    }

}