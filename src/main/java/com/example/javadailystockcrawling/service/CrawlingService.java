package com.example.javadailystockcrawling.service;

import com.example.javadailystockcrawling.dto.MarketIndex;
import com.example.javadailystockcrawling.dto.Stock;
import com.example.javadailystockcrawling.dto.UpperLimitStocks;
import com.example.javadailystockcrawling.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlingService {
    private final StockRepository stockRepository;


    // 상한가 종목 크롤링
    public UpperLimitStocks crawlingUppserLimitStocks() {
        List<Stock> kospiUpperLimitStocks = new ArrayList<>();
        List<Stock> kosdaqUpperLimitStocks = new ArrayList<>();

        try {
            String url = "https://finance.naver.com/sise/sise_upper.naver";
            Connection conn = Jsoup.connect(url);
            Document doc =  conn.get();
            // KOSPI 상한가 종목 크롤링
            Elements kospiElements = doc.select("#contentarea > div:nth-child(3) > table > tbody tr");
            kospiElements.forEach(trElement -> {
                Element thElement = trElement.selectFirst("td:nth-of-type(4) > a");
                if (thElement != null) {
                    String name = trElement.selectFirst("td:nth-of-type(4) > a").ownText();
                    String priceStr = trElement.selectFirst("td:nth-of-type(5)").ownText();
                    String updownStr = trElement.selectFirst("td:nth-of-type(6) > span").ownText().strip();
                    String rateStr = trElement.selectFirst("td:nth-of-type(7) > span").ownText().strip();
                    String volumeStr = trElement.selectFirst("td:nth-of-type(8)").ownText();

                    long price = Long.parseLong(priceStr.replaceAll(",", "").strip());
                    long updown = Long.parseLong(updownStr.replaceAll(",", "").strip());
                    double rate = Double.parseDouble(rateStr.replaceAll("%", "").strip());
                    long volume = Long.parseLong(volumeStr.replaceAll(",", "").strip());

                    Stock stock = new Stock();
                    stock.setName(name);
                    stock.setCode("testcode1");
                    stock.setMarket("kospi");
                    stock.setPrice(price);
                    stock.setUpdown(updown);
                    stock.setRate(rate);
                    stock.setVolume(volume);

                    kospiUpperLimitStocks.add(stock);
                }
                stockRepository.saveAll(kospiUpperLimitStocks);
            });

            // KOSDAQ 상한가 종목 크롤링
            Elements kosdaqElements = doc.select("#contentarea > div:nth-child(4) > table > tbody tr");
            kosdaqElements.forEach(trElement -> {
                Element thElement = trElement.selectFirst("td:nth-of-type(4) > a");
                if (thElement != null) {
                    String name = trElement.selectFirst("td:nth-of-type(4) > a").ownText();
                    String  priceStr = trElement.selectFirst("td:nth-of-type(5)").ownText();
                    String updownStr = trElement.selectFirst("td:nth-of-type(6) > span").ownText().strip();
                    String rateStr = trElement.selectFirst("td:nth-of-type(7) > span").ownText().strip();
                    String volumeStr = trElement.selectFirst("td:nth-of-type(8)").ownText();

                    long price = Long.parseLong(priceStr.replaceAll(",", "").strip());
                    long updown = Long.parseLong(updownStr.replaceAll(",", "").strip());
                    double rate = Double.parseDouble(rateStr.replaceAll("%", "").strip());
                    long volume = Long.parseLong(volumeStr.replaceAll(",", "").strip());

                    Stock stock = new Stock();
                    stock.setCode("testcode1");
                    stock.setName(name);
                    stock.setMarket("kosdaq");
                    stock.setPrice(price);
                    stock.setUpdown(updown);
                    stock.setRate(rate);
                    stock.setVolume(volume);

                    kosdaqUpperLimitStocks.add(stock);
                }
                stockRepository.saveAll(kosdaqUpperLimitStocks);
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new UpperLimitStocks(kospiUpperLimitStocks,kosdaqUpperLimitStocks);
    }


    // 시장 지표 크롤링
    public List<MarketIndex> crawlingMarketIndexes() {

        List<MarketIndex> marketIndexList = new ArrayList<>();
        List<MarketIndex> domesticMarketListIndex = crawlingDomesticIndexes();
        List<MarketIndex> usMarketListIndex = crawlingUsIndexes();
        List<MarketIndex> materialMarketListIndex = crawlingMaterialIndexes();

        marketIndexList.addAll(domesticMarketListIndex);
        marketIndexList.addAll(usMarketListIndex);
        marketIndexList.addAll(materialMarketListIndex);

        return marketIndexList;
    }

    private List<MarketIndex> crawlingMaterialIndexes() {
        List<MarketIndex> marketIndexList = new ArrayList<>();

        String url = "https://finance.naver.com/marketindex/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {
            MarketIndex dollorMarketIndex = new MarketIndex();
            dollorMarketIndex.setName(doc.selectFirst("#exchangeList > li.on > a.head.usd > h3 > span").ownText());
            dollorMarketIndex.setIvalue(Double.parseDouble(doc.selectFirst("#exchangeList > li.on > a.head.usd > div > span.value").ownText().replaceAll(",", "").strip()));
            dollorMarketIndex.setUpdown(Double.parseDouble(doc.selectFirst("#exchangeList > li:nth-child(1) > a.head.usd > div > span.change").ownText().replaceAll(",", "").strip()));
            marketIndexList.add(dollorMarketIndex);

            MarketIndex wtiMarketIndex = new MarketIndex();
            wtiMarketIndex.setName(doc.selectFirst("#oilGoldList > li.on > a.head.wti > h3 > span").ownText());
            wtiMarketIndex.setIvalue(Double.parseDouble(doc.selectFirst("#oilGoldList > li:nth-child(1) > a.head.wti > div > span.value").ownText().replaceAll(",", "").strip()));
            wtiMarketIndex.setUpdown(Double.parseDouble(doc.selectFirst("#oilGoldList > li:nth-child(1) > a.head.wti > div > span.value").ownText().replaceAll(",", "").strip()));
            marketIndexList.add(wtiMarketIndex);

            MarketIndex goldMarketIndex = new MarketIndex();
            goldMarketIndex.setName(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > h3 > span").ownText());
            goldMarketIndex.setIvalue(Double.parseDouble(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > div > span.value").ownText().replaceAll(",", "").strip()));
            goldMarketIndex.setUpdown(Double.parseDouble(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > div > span.change").ownText().replaceAll(",", "").strip()));
            marketIndexList.add(goldMarketIndex);

        }
        return marketIndexList;
    }

    private List<MarketIndex> crawlingUsIndexes() {
        List<MarketIndex> marketIndexList = new ArrayList<>();

        String url = "https://finance.naver.com/world/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {
            MarketIndex dowMarketIndex = new MarketIndex();
            dowMarketIndex.setName(doc.selectFirst("#worldIndexColumn1 > li.on > dl > dt > a > span").ownText());
            dowMarketIndex.setIvalue(Double.parseDouble(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > strong").ownText().replaceAll(",", "").strip()));
            dowMarketIndex.setUpdown(Double.parseDouble(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > em").ownText().replaceAll(",", "").strip()));
            dowMarketIndex.setRate(Double.parseDouble(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText().replaceAll("%", "").strip()));
            marketIndexList.add(dowMarketIndex);

            MarketIndex nasdaqMarketIndex = new MarketIndex();
            nasdaqMarketIndex.setName(doc.selectFirst("#worldIndexColumn2 > li.on > dl > dt > a > span").ownText());
            nasdaqMarketIndex.setIvalue(Double.parseDouble(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > strong").ownText().replaceAll(",", "").strip()));
            nasdaqMarketIndex.setUpdown(Double.parseDouble(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > em").ownText().replaceAll(",", "").strip()));
            nasdaqMarketIndex.setRate(Double.parseDouble(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText().replaceAll("%", "").strip()));
            marketIndexList.add(nasdaqMarketIndex);

            MarketIndex snpMarketIndex = new MarketIndex();
            snpMarketIndex.setName(doc.selectFirst("#worldIndexColumn3 > li.on > dl > dt > a > span").ownText());
            snpMarketIndex.setIvalue(Double.parseDouble(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > strong").ownText().replaceAll(",", "").strip()));
            snpMarketIndex.setUpdown(Double.parseDouble(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > em").ownText().replaceAll(",", "").strip()));
            snpMarketIndex.setRate(Double.parseDouble(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText().replaceAll("%", "").strip()));
            marketIndexList.add(snpMarketIndex);

        }
        return marketIndexList;
    }

    private List<MarketIndex> crawlingDomesticIndexes() {
        List<MarketIndex> marketIndexList = new ArrayList<>();

        String url = "https://finance.naver.com/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {
            MarketIndex kospiMarketIndex = new MarketIndex();
            kospiMarketIndex.setName(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > h4 > a > em > span").ownText());
            kospiMarketIndex.setIvalue(Double.parseDouble(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num").ownText().replaceAll(",", "").strip()));
            kospiMarketIndex.setUpdown(Double.parseDouble(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num2").ownText().replaceAll(",", "").strip()));
            kospiMarketIndex.setRate(Double.parseDouble(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num3").ownText().replaceAll("%", "").strip()));
            marketIndexList.add(kospiMarketIndex);

            MarketIndex kosdaqMarketIndex = new MarketIndex();
            kosdaqMarketIndex.setName(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > h4 > a > em > span").ownText());
            kosdaqMarketIndex.setIvalue(Double.parseDouble(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num").ownText().replaceAll(",", "").strip()));
            kosdaqMarketIndex.setUpdown(Double.parseDouble(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num").ownText().replaceAll(",", "").strip()));
            kosdaqMarketIndex.setRate(Double.parseDouble(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num3").ownText().replaceAll("%", "").strip()));
            marketIndexList.add(kosdaqMarketIndex);

        }
        return marketIndexList;
    }

    private Document getDocumentFromConnection(String url) {
        Document doc;
        Connection conection = Jsoup.connect(url);
        try {
            doc = conection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return doc;
    }
}
