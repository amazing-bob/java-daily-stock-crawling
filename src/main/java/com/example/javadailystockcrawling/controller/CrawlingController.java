package com.example.javadailystockcrawling.controller;

import com.example.javadailystockcrawling.annotation.Auth;
import com.example.javadailystockcrawling.dto.Index;
import com.example.javadailystockcrawling.dto.Stock;
import com.example.javadailystockcrawling.dto.UpperLimitStocks;
import com.example.javadailystockcrawling.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crawling")
@RequiredArgsConstructor
@Auth
@Slf4j
public class CrawlingController {
    private final CrawlingService crawlingService;

    @GetMapping("")
    public ResponseEntity crawlingAll() {
        System.out.println("\n시황\n---------------");
        List<Index> marketIndexes = crawlingService.crawlingMarketIndexes();
        for (Index index : marketIndexes) {
            System.out.println(index);
        }

        System.out.println("\n\n상한가\n---------------");
        UpperLimitStocks upperLimitStocks = crawlingService.crawlingUppserLimitStocks();
        for ( Stock stock : upperLimitStocks.getKospiUpperLimitStocks()) {
            System.out.println(stock);
        }
        System.out.println("---------------");
        for ( Stock stock : upperLimitStocks.getKosdaqUpperLimitStocks()) {
            System.out.println(stock);
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
    public ResponseEntity<List<Index>> crawlingMarketIndexes() throws Exception {
        log.info("======== /api/crawling/market-indexes");
        List<Index> marketIndexes = crawlingService.crawlingMarketIndexes();
        return ResponseEntity
                .ok()
                .body(marketIndexes);
    }
}
