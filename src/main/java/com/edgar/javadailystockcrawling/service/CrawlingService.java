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


enum MarketType {
    KOSPI("코스피"),
    KOSDAQ("코스닥");

    private String marketName;

    MarketType(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketName() {
        return marketName;
    }
}

enum UsIndexType {
    DOW("#worldIndexColumn1"),
    NASDAQ("#worldIndexColumn2"),
    SP500("#worldIndexColumn3");

    private String selectorId;

    UsIndexType(String selectorId) {
        this.selectorId = selectorId;
    }

    public String getSelectorId() {
        return selectorId;
    }
}


@Service
@RequiredArgsConstructor
@Slf4j
public class CrawlingService {

    private final StockRepository stockRepository;
    private final MarketIndexRepository marketIndexRepository;

    /**
     * 상한가 종목 크롤링
     * @return
     */
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


    /**
     * 시장 지수들 크롤링
     * @return
     */
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


    /**
     * 원자재 지수 크롤링
     * @return
     */
    private List<MarketIndex> crawlingMaterialIndexes() {
        List<MarketIndex> marketIndexList = new ArrayList<>();

        String url = "https://finance.naver.com/marketindex/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {

            String name = doc.selectFirst("#exchangeList > li.on > a.head.usd > h3 > span").ownText();
            String value = doc.selectFirst("#exchangeList > li.on > a.head.usd > div > span.value").ownText().replaceAll(",", "").strip();
            String updown = doc.selectFirst("#exchangeList > li:nth-child(1) > a.head.usd > div > span.change").ownText().replaceAll(",", "").strip();
            boolean isPlus = doc.selectFirst("#exchangeList > li.on > a.head.usd > div").attr("class").contains("point_up");
            //TODO: 원자재 지수 크롤링 계속 구현해 나가야 함. 아예 상세페이지로 들어가서 파싱해오는게 좋을 수도 있겠음

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


    /**
     * 미국 지수 크롤링
     * @return
     */
    private List<MarketIndex> crawlingUsIndexes() {
        List<MarketIndex> marketIndexList = new ArrayList<>();

        String url = "https://finance.naver.com/world/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {

            MarketIndex dowMarketIndex = parseUsMarketIndex(doc, UsIndexType.DOW.getSelectorId());
            MarketIndex nasdaqMarketIndex = parseUsMarketIndex(doc, UsIndexType.NASDAQ.getSelectorId());
            MarketIndex sp500MarketIndex = parseUsMarketIndex(doc, UsIndexType.SP500.getSelectorId());
            marketIndexList.add(dowMarketIndex);
            marketIndexList.add(nasdaqMarketIndex);
            marketIndexList.add(sp500MarketIndex);

            System.out.println("marketIndexList = " + marketIndexList);
        }
        return marketIndexList;
    }

    /**
     * 미국 지수 파싱
     * @param doc
     * @return
     */
    private static MarketIndex parseUsMarketIndex(Document doc, String selectocId) {
        String name = doc.selectFirst(selectocId + " > li.on > dl > dt > a > span").ownText();
        String value = doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > strong").ownText().replaceAll(",", "").strip();
        String updown = doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > em").ownText().replaceAll(",", "").strip();
        String rate = doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3)").ownText().replaceAll("%", "").strip();
        boolean isPlus = doc.selectFirst("#worldIndexColumn1 > li:nth-child(1) > dl > dd.point_status > span:nth-child(3) > span").ownText().contains("+");

        MarketIndex marketIndex = new MarketIndex();
        marketIndex.setName(name);
        marketIndex.setIvalue(Double.parseDouble(value));
        marketIndex.setUpdown(Double.parseDouble(updown) * (isPlus ? 1 : -1));
        marketIndex.setRate(Double.parseDouble(rate));
        return marketIndex;
    }


    /**
     * 국내지수 크롤링
     * @return
     */
    private List<MarketIndex> crawlingDomesticIndexes() {
        List<MarketIndex> marketIndexList = new ArrayList<>();

        String url = "https://finance.naver.com/sise/";
        Document doc = getDocumentFromConnection(url);
        if (doc != null) {
            MarketIndex kospiMarketIndex = parseDomesticIndex(doc, MarketType.KOSPI.name(), MarketType.KOSPI.getMarketName());
            MarketIndex kosdaqMarketIndex = parseDomesticIndex(doc, MarketType.KOSDAQ.name(), MarketType.KOSDAQ.getMarketName());
            marketIndexList.add(kospiMarketIndex);
            marketIndexList.add(kosdaqMarketIndex);
        }
        return marketIndexList;
    }

    /**
     * 국내지수 파싱
     * @param doc
     * @param marketType
     * @param marketName
     * @return
     */
    private static MarketIndex parseDomesticIndex(Document doc, String marketType, String marketName) {
        MarketIndex kospiMarketIndex = new MarketIndex();
        String valueStr = doc.selectFirst("#%s_now".formatted(marketType)).ownText();
        boolean isPlus = doc.selectFirst("#%s_change > span".formatted(marketType)).attr("class").contains("up");
        String updownRateStr = doc.selectFirst("#%s_change".formatted(marketType)).ownText();
        String updownStr = updownRateStr.split(" ")[0];
        String rateStr = updownRateStr.split(" ")[1];

        kospiMarketIndex.setName(marketName);
        kospiMarketIndex.setIvalue(Double.parseDouble(valueStr.replaceAll(",", "").strip()));
        kospiMarketIndex.setUpdown(Double.parseDouble(updownStr.replaceAll(",", "").strip()) * (isPlus ? 1 : -1));
        kospiMarketIndex.setRate(Double.parseDouble(rateStr.replaceAll("%", "").strip()));

        return kospiMarketIndex;
    }

    /**
     *
     * @param url
     * @return
     */
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
