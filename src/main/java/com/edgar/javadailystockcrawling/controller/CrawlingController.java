package com.edgar.javadailystockcrawling.controller;

import com.edgar.javadailystockcrawling.annotation.Auth;
import com.edgar.javadailystockcrawling.dto.MarketIndex;
import com.edgar.javadailystockcrawling.service.CrawlingService;
import com.edgar.javadailystockcrawling.dto.StockPriceInfo;
import com.edgar.javadailystockcrawling.dto.UpperLimitStocks;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crawling")
@RequiredArgsConstructor
//@Auth //Authorization 적용
@Slf4j
public class CrawlingController {
    private final CrawlingService crawlingService;

    @GetMapping("")
    public ResponseEntity crawlingAll() {
        System.out.println("\n시황\n---------------");
        List<MarketIndex> marketMarketIndexIndices = crawlingService.crawlingMarketIndexes();
        for (MarketIndex marketIndex : marketMarketIndexIndices) {
            System.out.println(marketIndex);
        }

        System.out.println("\n\n상한가\n---------------");
        UpperLimitStocks upperLimitStocks = crawlingService.crawlingUppserLimitStocks();
        for ( StockPriceInfo stockPriceInfo : upperLimitStocks.getKospiUpperLimitStocks()) {
            System.out.println(stockPriceInfo);
        }
        System.out.println("---------------");
        for ( StockPriceInfo stockPriceInfo : upperLimitStocks.getKosdaqUpperLimitStocks()) {
            System.out.println(stockPriceInfo);
        }

        return ResponseEntity
                .ok()
                .body("SUCCESS!!");
    }

    @RequestMapping(path="/upper-limit-stocks", method = RequestMethod.GET)
    public ResponseEntity<UpperLimitStocks> crawlingUppserLimitStocks() {
        log.info("======== /api/crawling/upper-limit-stocks");
        UpperLimitStocks upperLimitStocks = crawlingService.crawlingUppserLimitStocks();
        return ResponseEntity
                .ok()
                .body(upperLimitStocks);
    }

    @GetMapping("/market-indexes")
    public ResponseEntity<List<MarketIndex>> crawlingMarketIndexes() throws Exception {
        log.info("======== /api/crawling/market-indexes");
        List<MarketIndex> marketMarketIndexIndices = crawlingService.crawlingMarketIndexes();
        return ResponseEntity
                .ok()
                .body(marketMarketIndexIndices);
    }
}
