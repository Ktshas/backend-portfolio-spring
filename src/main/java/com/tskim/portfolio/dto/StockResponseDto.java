package com.tskim.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class StockResponseDto {
    
    @JsonProperty("pollingInterval")
    private Integer pollingInterval;
    
    @JsonProperty("datas")
    private List<StockData> datas;
    
    @JsonProperty("time")
    private String time;
    
    @Data
    public static class StockData {
        @JsonProperty("itemCode")
        private String itemCode;
        
        @JsonProperty("stockName")
        private String stockName;
        
        @JsonProperty("closePrice")
        private String closePrice;
        
        @JsonProperty("compareToPreviousClosePrice")
        private String compareToPreviousClosePrice;
        
        @JsonProperty("compareToPreviousPrice")
        private CompareToPreviousPrice compareToPreviousPrice;
        
        @JsonProperty("fluctuationsRatio")
        private String fluctuationsRatio;
        
        @JsonProperty("openPrice")
        private String openPrice;
        
        @JsonProperty("highPrice")
        private String highPrice;
        
        @JsonProperty("lowPrice")
        private String lowPrice;
        
        @JsonProperty("accumulatedTradingVolume")
        private String accumulatedTradingVolume;
        
        @JsonProperty("accumulatedTradingValue")
        private String accumulatedTradingValue;
        
        @JsonProperty("marketStatus")
        private String marketStatus;
        
        @JsonProperty("localTradedAt")
        private String localTradedAt;
        
        @JsonProperty("currencyType")
        private CurrencyType currencyType;
        
        @JsonProperty("stockExchangeType")
        private StockExchangeType stockExchangeType;
        
        @JsonProperty("tradeStopType")
        private TradeStopType tradeStopType;
        
        @JsonProperty("overMarketPriceInfo")
        private Object overMarketPriceInfo;
        
        @JsonProperty("isinCode")
        private String isinCode;
        
        @JsonProperty("myDataCode")
        private Object myDataCode;
        
        @JsonProperty("stockEndUrl")
        private Object stockEndUrl;
        
        @JsonProperty("symbolCode")
        private String symbolCode;
    }
    
    @Data
    public static class CompareToPreviousPrice {
        @JsonProperty("code")
        private String code;
        
        @JsonProperty("text")
        private String text;
        
        @JsonProperty("name")
        private String name;
    }
    
    @Data
    public static class CurrencyType {
        @JsonProperty("code")
        private String code;
        
        @JsonProperty("text")
        private String text;
        
        @JsonProperty("name")
        private String name;
    }
    
    @Data
    public static class StockExchangeType {
        @JsonProperty("code")
        private String code;
        
        @JsonProperty("zoneId")
        private String zoneId;
        
        @JsonProperty("nationType")
        private String nationType;
        
        @JsonProperty("delayTime")
        private Integer delayTime;
        
        @JsonProperty("startTime")
        private String startTime;
        
        @JsonProperty("endTime")
        private String endTime;
        
        @JsonProperty("closePriceSendTime")
        private String closePriceSendTime;
        
        @JsonProperty("nameKor")
        private String nameKor;
        
        @JsonProperty("nameEng")
        private String nameEng;
        
        @JsonProperty("stockType")
        private String stockType;
        
        @JsonProperty("nationCode")
        private String nationCode;
        
        @JsonProperty("nationName")
        private String nationName;
        
        @JsonProperty("name")
        private String name;
    }
    
    @Data
    public static class TradeStopType {
        @JsonProperty("code")
        private String code;
        
        @JsonProperty("text")
        private String text;
        
        @JsonProperty("name")
        private String name;
    }
}
