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
                    String  priceStr = trElement.selectFirst("td:nth-of-type(5)").ownText();
                    String updownStr = trElement.selectFirst("td:nth-of-type(6) > span").ownText().strip();
                    String rateStr = trElement.selectFirst("td:nth-of-type(7) > span").ownText().strip();
                    String volumeStr = trElement.selectFirst("td:nth-of-type(8)").ownText();

                    long price = Long.parseLong(priceStr.replaceAll(",", "").strip());
                    long updown = Long.parseLong(updownStr.replaceAll(",", "").strip());
                    double rate = Double.parseDouble(rateStr.replaceAll("%", "").strip());
                    long volume = Long.parseLong(volumeStr.replaceAll(",", "").strip());

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
            dollorIndex.setValue(Long.parseLong(doc.selectFirst("#exchangeList > li.on > a.head.usd > div > span.value").ownText().strip()));
            dollorIndex.setUpdown(Long.parseLong(doc.selectFirst("#exchangeList > li:nth-child(1) > a.head.usd > div > span.change").ownText().strip()));
            indexList.add(dollorIndex);

            Index wtiIndex = new Index();
            wtiIndex.setName(doc.selectFirst("#oilGoldList > li.on > a.head.wti > h3 > span").ownText());
            wtiIndex.setValue(Long.parseLong(doc.selectFirst("#oilGoldList > li:nth-child(1) > a.head.wti > div > span.value").ownText().strip()));
            wtiIndex.setUpdown(Long.parseLong(doc.selectFirst("#oilGoldList > li:nth-child(1) > a.head.wti > div > span.value").ownText().strip()));
            indexList.add(wtiIndex);

            Index goldIndex = new Index();
            goldIndex.setName(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > h3 > span").ownText());
            goldIndex.setValue(Long.parseLong(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > div > span.value").ownText().strip()));
            goldIndex.setUpdown(Long.parseLong(doc.selectFirst("#oilGoldList > li:nth-child(3) > a.head.gold_inter > div > span.change").ownText().strip()));
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
            dowIndex.setValue(Long.parseLong(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > strong").ownText().strip()));
            dowIndex.setUpdown(Long.parseLong(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > em").ownText().strip()));
            dowIndex.setRate(Double.parseDouble(doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText().strip()));
            indexList.add(dowIndex);

            Index nasdaqIndex = new Index();
            nasdaqIndex.setName(doc.selectFirst("#worldIndexColumn2 > li.on > dl > dt > a > span").ownText());
            nasdaqIndex.setValue(Long.parseLong(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > strong").ownText().strip()));
            nasdaqIndex.setUpdown(Long.parseLong(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > em").ownText().strip()));
            nasdaqIndex.setRate(Double.parseDouble(doc.selectFirst("#worldIndexColumn2 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText()));
            indexList.add(nasdaqIndex);

            Index snpIndex = new Index();
            snpIndex.setName(doc.selectFirst("#worldIndexColumn3 > li.on > dl > dt > a > span").ownText());
            snpIndex.setValue(Long.parseLong(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > strong").ownText().strip()));
            snpIndex.setUpdown(Long.parseLong(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > em").ownText().strip()));
            snpIndex.setRate(Double.parseDouble(doc.selectFirst("#worldIndexColumn3 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText().strip()));
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
            kospiIndex.setValue(Long.parseLong(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num").ownText().strip()));
            kospiIndex.setUpdown(Long.parseLong(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num2").ownText().strip()));
            kospiIndex.setRate(Double.parseDouble(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kospi_area.group_quot.quot_opn > div.heading_area > a > span > span.num3").ownText().strip()));
            indexList.add(kospiIndex);

            Index kosdaqIndex = new Index();
            kosdaqIndex.setName(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > h4 > a > em > span").ownText());
            kosdaqIndex.setValue(Long.parseLong(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num").ownText().strip()));
            kosdaqIndex.setUpdown(Long.parseLong(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num").ownText().strip()));
            kosdaqIndex.setRate(Double.parseDouble(doc.selectFirst("#content > div.article > div.section2 > div.section_stock_market > div.section_stock > div.kosdaq_area.group_quot > div.heading_area > a > span > span.num3").ownText()));
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
