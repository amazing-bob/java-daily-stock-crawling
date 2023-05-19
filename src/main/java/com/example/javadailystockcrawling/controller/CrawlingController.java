package com.example.javadailystockcrawling.controller;

import com.example.javadailystockcrawling.dto.Index;
import com.example.javadailystockcrawling.dto.Stock;
import com.example.javadailystockcrawling.dto.UpperLimitStocks;
import com.example.javadailystockcrawling.service.CrawlingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crawling")
//@RequiredArgsConstructor
public class CrawlingController {
    private CrawlingService crawlingService;

    public CrawlingController(CrawlingService crawlingService) {
        this.crawlingService = crawlingService;
    }

    @GetMapping("")
    public void crawlingAll() {
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
    }

    @RequestMapping(path="/upper-limit-stocks", method = RequestMethod.GET)
    public UpperLimitStocks crawlingUppserLimitStocks() {
        System.out.println("======== /api/crawling/upper-limit-stocks");
        UpperLimitStocks upperLimitStocks = crawlingService.crawlingUppserLimitStocks();
        return upperLimitStocks;
    }

    @GetMapping("/market-indexes")
    public List<Index> crawlingMarketIndexes() {
        System.out.println("======== /api/crawling/market-indexes");
        List<Index> marketIndexes = crawlingService.crawlingMarketIndexes();
        return marketIndexes;
    }
}
