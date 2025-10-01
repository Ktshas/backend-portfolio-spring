package com.tskim.portfolio.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockInfoDto {
    
    /**
     * 종목 코드
     */
    private String itemCode;
    
    /**
     * 종목명
     */
    private String stockName;
    
    /**
     * 현재가
     */
    private String closePrice;
    
    /**
     * 전일 대비 가격 변동
     */
    private String compareToPreviousClosePrice;
    
    /**
     * 전일 대비 변동률 (%)
     */
    private String fluctuationsRatio;
    
    /**
     * 전일 대비 변동 방향 (상승/하락/보합)
     */
    private String compareDirection;
    
    /**
     * 시가
     */
    private String openPrice;
    
    /**
     * 고가
     */
    private String highPrice;
    
    /**
     * 저가
     */
    private String lowPrice;
    
    /**
     * 거래량
     */
    private String accumulatedTradingVolume;
    
    /**
     * 거래대금
     */
    private String accumulatedTradingValue;
    
    /**
     * 시장 상태 (OPEN/CLOSE)
     */
    private String marketStatus;
    
    /**
     * 거래 시간
     */
    private String localTradedAt;
    
    /**
     * 환율 타입 (KRW/USD 등)
     */
    private String currencyType;
    
    /**
     * 목표가
     */
    private String targetPrice;
    
    /**
     * 전일 대비 변동 방향을 한국어로 변환
     */
    public String getCompareDirectionKorean() {
        if (compareDirection == null) return "정보없음";
        
        return switch (compareDirection) {
            case "RISING" -> "상승";
            case "FALLING" -> "하락";
            case "UNCHANGED" -> "보합";
            default -> "정보없음";
        };
    }
    
    /**
     * 시장 상태를 한국어로 변환
     */
    public String getMarketStatusKorean() {
        if (marketStatus == null) return "정보없음";
        
        return switch (marketStatus) {
            case "OPEN" -> "거래중";
            case "CLOSE" -> "거래종료";
            default -> "정보없음";
        };
    }
}
