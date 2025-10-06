package com.tskim.portfolio.dto.crypto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CryptoResponseDto {
    
    @JsonProperty("market")
    private String market;
    
    @JsonProperty("trade_date")
    private String tradeDate;
    
    @JsonProperty("trade_time")
    private String tradeTime;
    
    @JsonProperty("trade_date_kst")
    private String tradeDateKst;
    
    @JsonProperty("trade_time_kst")
    private String tradeTimeKst;
    
    @JsonProperty("trade_timestamp")
    private Long tradeTimestamp;
    
    @JsonProperty("opening_price")
    private String openingPrice;
    
    @JsonProperty("high_price")
    private String highPrice;
    
    @JsonProperty("low_price")
    private String lowPrice;
    
    @JsonProperty("trade_price")
    private String tradePrice;
    
    @JsonProperty("prev_closing_price")
    private String prevClosingPrice;
    
    @JsonProperty("change")
    private String change;
    
    @JsonProperty("change_price")
    private String changePrice;
    
    @JsonProperty("change_rate")
    private String changeRate;
    
    @JsonProperty("signed_change_price")
    private String signedChangePrice;
    
    @JsonProperty("signed_change_rate")
    private String signedChangeRate;
    
    @JsonProperty("trade_volume")
    private String tradeVolume;
    
    @JsonProperty("acc_trade_price")
    private String accTradePrice;
    
    @JsonProperty("acc_trade_price_24h")
    private String accTradePrice24h;
    
    @JsonProperty("acc_trade_volume")
    private String accTradeVolume;
    
    @JsonProperty("acc_trade_volume_24h")
    private String accTradeVolume24h;
    
    @JsonProperty("highest_52_week_price")
    private String highest52WeekPrice;
    
    @JsonProperty("highest_52_week_date")
    private String highest52WeekDate;
    
    @JsonProperty("lowest_52_week_price")
    private String lowest52WeekPrice;
    
    @JsonProperty("lowest_52_week_date")
    private String lowest52WeekDate;
    
    @JsonProperty("timestamp")
    private Long timestamp;
}
