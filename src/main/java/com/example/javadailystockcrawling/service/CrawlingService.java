package com.example.javadailystockcrawling.service;

import com.example.javadailystockcrawling.dto.Index;
import com.example.javadailystockcrawling.dto.Stock;
import com.example.javadailystockcrawling.dto.UpperLimitStocks;
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
public class CrawlingService {
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
                    String  price = trElement.selectFirst("td:nth-of-type(5)").ownText();
                    String updown = trElement.selectFirst("td:nth-of-type(6) > span").ownText().strip();
                    String rate = trElement.selectFirst("td:nth-of-type(7) > span").ownText().strip();
                    String volume = trElement.selectFirst("td:nth-of-type(8)").ownText();

                    Stock stock = new Stock();
                    stock.setName(name);
                    stock.setPrice(price);
                    stock.setUpdown(updown);
                    stock.setRate(rate);
                    stock.setVolume(volume);

                    kospiUpperLimitStocks.add(stock);
                }

            });

            // KOSDAQ 상한가 종목 크롤링
            Elements kosdaqElements = doc.select("#contentarea > div:nth-child(4) > table > tbody tr");
            kosdaqElements.forEach(trElement -> {
                Element thElement = trElement.selectFirst("td:nth-of-type(4) > a");
                if (thElement != null) {
                    String name = trElement.selectFirst("td:nth-of-type(4) > a").ownText();
                    String  price = trElement.selectFirst("td:nth-of-type(5)").ownText();
                    String updown = trElement.selectFirst("td:nth-of-type(6) > span").ownText().strip();
                    String rate = trElement.selectFirst("td:nth-of-type(7) > span").ownText().strip();
                    String volume = trElement.selectFirst("td:nth-of-type(8)").ownText();

                    Stock stock = new Stock();
                    stock.setName(name);
                    stock.setPrice(price);
                    stock.setUpdown(updown);
                    stock.setRate(rate);
                    stock.setVolume(volume);

                    kosdaqUpperLimitStocks.add(stock);
                }

            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new UpperLimitStocks(kospiUpperLimitStocks,kosdaqUpperLimitStocks);
    }


    // 시장 지표 크롤링
    public List<Index> crawlingMarketIndexes() {

        List<Index> indexList = new ArrayList<>();
        List<Index> domesticIndexList = crawlingDomesticIndexes();
        List<Index> usIndexList = crawlingUsIndexes();
        List<Index> materialIndexList = crawlingMaterialIndexes();

        indexList.addAll(domesticIndexList);
        indexList.addAll(usIndexList);
        indexList.addAll(materialIndexList);

        return indexList;
    }

    private List<Index> crawlingMaterialIndexes() {
        List<Index> indexList = new ArrayList<>();

        String url = "https://finance.naver.com/marketindex/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {
            Index dollorIndex = new Index();
            dollorIndex.setName(doc.selectFirst("#exchangeList > li.on > a.head.usd > h3 > span").ownText());
            dollorIndex.setValue(doc.selectFirst("#exchangeList > li.on > a.head.usd > div > span.value").ownText());
            dollorIndex.setUpdown(doc.selectFirst("#exchangeList > li:nth-child(1) > a.head.usd > div > span.change").ownText());
            indexList.add(dollorIndex);

            Index wtiIndex = new Index();
            wtiIndex.setName(doc.selectFirst("#oilGoldList > li.on > a.head.wti > h3 > span").ownText());
            wtiIndex.setValue(doc.selectFirst("#oilGoldList > li:nth-child(1) > a.head.wti > div > span.value").ownText());
            wtiIndex.setUpdown(doc.selectFirst("#oilGoldList > li:nth-child(1) > a.head.wti > div > span.value").ownText());
            indexList.add(wtiIndex);

            Index goldIndex = new Index();
            goldIndex.setName(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > h3 > span").ownText());
            goldIndex.setValue(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > div > span.value").ownText());
            goldIndex.setUpdown(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > div > span.change").ownText());
            indexList.add(goldIndex);

        }
        return indexList;
    }

    private List<Index> crawlingUsIndexes() {
        List<Index> indexList = new ArrayList<>();

        String url = "https://finance.naver.com/world/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {
            Index dowIndex = new Index();
            dowIndex.setName(doc.selectFirst("#worldIndexColumn1 > li.on > dl > dt > a > span").ownText());
            dowIndex.setValue(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > strong").ownText());
            dowIndex.setUpdown(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > em").ownText());
            dowIndex.setRate(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText());
            indexList.add(dowIndex);

            Index nasdaqIndex = new Index();
            nasdaqIndex.setName(doc.selectFirst("#worldIndexColumn2 > li.on > dl > dt > a > span").ownText());
            nasdaqIndex.setValue(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > strong").ownText());
            nasdaqIndex.setUpdown(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > em").ownText());
            nasdaqIndex.setRate(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText());
            indexList.add(nasdaqIndex);

            Index snpIndex = new Index();
            snpIndex.setName(doc.selectFirst("#worldIndexColumn3 > li.on > dl > dt > a > span").ownText());
            snpIndex.setValue(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > strong").ownText());
            snpIndex.setUpdown(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > em").ownText());
            snpIndex.setRate(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText());
            indexList.add(snpIndex);

        }
        return indexList;
    }

    private List<Index> crawlingDomesticIndexes() {
        List<Index> indexList = new ArrayList<>();

        String url = "https://finance.naver.com/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {
            Index kospiIndex = new Index();
            kospiIndex.setName(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > h4 > a > em > span").ownText());
            kospiIndex.setValue(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num").ownText());
            kospiIndex.setUpdown(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num2").ownText());
            kospiIndex.setRate(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num3").ownText());
            indexList.add(kospiIndex);

            Index kosdaqIndex = new Index();
            kosdaqIndex.setName(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > h4 > a > em > span").ownText());
            kosdaqIndex.setValue(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num").ownText());
            kosdaqIndex.setUpdown(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num").ownText());
            kosdaqIndex.setRate(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num3").ownText());
            indexList.add(kosdaqIndex);

        }
        return indexList;
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
