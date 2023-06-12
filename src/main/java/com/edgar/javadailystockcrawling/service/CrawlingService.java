package com.edgar.javadailystockcrawling.service;

import com.edgar.javadailystockcrawling.dto.MarketIndex;
import com.edgar.javadailystockcrawling.dto.StockPriceInfo;
import com.edgar.javadailystockcrawling.dto.UpperLimitStocks;
import com.edgar.javadailystockcrawling.repository.MarketIndexRepository;
import com.edgar.javadailystockcrawling.repository.StockRepository;
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
    private final MarketIndexRepository marketIndexRepository;

    // 상한가 종목 크롤링
    public UpperLimitStocks crawlingUppserLimitStocks() {
        List<StockPriceInfo> kospiUpperLimitStockPriceInfos = new ArrayList<>();
        List<StockPriceInfo> kosdaqUpperLimitStockPriceInfos = new ArrayList<>();

        try {
            String url = "https://finance.naver.com/sise/sise_upper.naver";
            Connection conn = Jsoup.connect(url);
            Document doc =  conn.get();

            // KOSPI 상한가 종목 크롤링
            Elements kospiElements = doc.select("#contentarea > div:nth-child(3) > table > tbody tr");
            kospiElements.forEach(trElement -> {
                Element thElement = trElement.selectFirst("td:nth-of-type(4) > a");
                if (thElement != null) {
                    // 상한가 종목 정보 파싱
                    StockPriceInfo stockPriceInfo = parseUpperLimitStockPriceInfo(trElement, StockPriceInfo.MARKET_KOSPI);
                    kospiUpperLimitStockPriceInfos.add(stockPriceInfo);
                }
                stockRepository.saveAll(kospiUpperLimitStockPriceInfos);
            });

            // KOSDAQ 상한가 종목 크롤링
            Elements kosdaqElements = doc.select("#contentarea > div:nth-child(4) > table > tbody tr");
            kosdaqElements.forEach(trElement -> {
                Element thElement = trElement.selectFirst("td:nth-of-type(4) > a");
                if (thElement != null) {
                    StockPriceInfo stockPriceInfo = parseUpperLimitStockPriceInfo(trElement, StockPriceInfo.MARKET_KOSDAQ);

                    kosdaqUpperLimitStockPriceInfos.add(stockPriceInfo);
                }
                stockRepository.saveAll(kosdaqUpperLimitStockPriceInfos);
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return new UpperLimitStocks(kospiUpperLimitStockPriceInfos, kosdaqUpperLimitStockPriceInfos);
    }

    /**
     * 상한가 종목 정보 파싱
     * @param trElement
     * @param marketType
     * @return StockPriceInfo
     */
    private StockPriceInfo parseUpperLimitStockPriceInfo(Element trElement, String marketType) {
        String name = trElement.selectFirst("td:nth-of-type(4) > a").ownText();
        String code = String.valueOf(trElement.selectFirst("td:nth-of-type(4) > a").attr("href")).split("code=")[1];
        String priceStr = trElement.selectFirst("td:nth-of-type(5)").ownText();
        String updownStr = trElement.selectFirst("td:nth-of-type(6) > span").ownText().strip();
        boolean isUp = trElement.selectFirst("td:nth-of-type(6) > span").attr("class").contains("red02");
        String rateStr = trElement.selectFirst("td:nth-of-type(7) > span").ownText().strip();
        String volumeStr = trElement.selectFirst("td:nth-of-type(8)").ownText();

        long price = Long.parseLong(priceStr.replaceAll(",", "").strip());
        long updown = Long.parseLong(updownStr.replaceAll(",", "").strip());
        if (!isUp) updown *= -1;
        double rate = Double.parseDouble(rateStr.replaceAll("%", "").strip());
        long volume = Long.parseLong(volumeStr.replaceAll(",", "").strip());

        StockPriceInfo stockPriceInfo = new StockPriceInfo();
        stockPriceInfo.setKind(StockPriceInfo.KIND_UPPER_LIMIT);
        stockPriceInfo.setName(name);
        stockPriceInfo.setCode(code);
        stockPriceInfo.setMarket(marketType);
        stockPriceInfo.setPrice(price);
        stockPriceInfo.setUpdown(updown);
        stockPriceInfo.setRate(rate);
        stockPriceInfo.setVolume(volume);
        return stockPriceInfo;
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

        marketIndexRepository.saveAll(marketIndexList);


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
