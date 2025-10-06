package com.tskim.portfolio.dto.crypto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptoInfoDto {
    
    /**
     * 암호화폐 코드 (예: KRW-BTC)
     */
    private String cryptoCode;
    
    /**
     * 암호화폐명
     */
    private String cryptoName;
    
    /**
     * 현재가
     */
    private String tradePrice;
    
    /**
     * 전일 대비 가격 변동
     */
    private String changePrice;
    
    /**
     * 전일 대비 변동률 (%)
     */
    private String changeRate;
    
    /**
     * 전일 대비 변동 방향 (상승/하락/보합)
     */
    private String change;
    
    /**
     * 시가
     */
    private String openingPrice;
    
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
    private String tradeVolume;
    
    /**
     * 거래대금
     */
    private String accTradePrice;
    
    /**
     * 24시간 거래대금
     */
    private String accTradePrice24h;
    
    /**
     * 24시간 거래량
     */
    private String accTradeVolume24h;
    
    /**
     * 52주 최고가
     */
    private String highest52WeekPrice;
    
    /**
     * 52주 최저가
     */
    private String lowest52WeekPrice;
    
    /**
     * 거래 시간
     */
    private String tradeTimeKst;
    
    /**
     * 목표가
     */
    private String targetPrice;
    
    /**
     * 목표가 알림 방향 (UP: 이상일 때 알림, DOWN: 이하일 때 알림)
     */
    private String targetPriceDirection;
    
    /**
     * 전일 대비 변동 방향을 한국어로 변환
     */
    public String getChangeKorean() {
        if (change == null) return "정보없음";
        
        return switch (change) {
            case "RISE" -> "상승";
            case "FALL" -> "하락";
            case "EVEN" -> "보합";
            default -> "정보없음";
        };
    }
    
    /**
     * 목표가 알림 방향을 한국어로 변환
     */
    public String getTargetPriceDirectionKorean() {
        if (targetPriceDirection == null) return "정보없음";
        
        return switch (targetPriceDirection) {
            case "UP" -> "이상";
            case "DOWN" -> "이하";
            default -> "정보없음";
        };
    }
}
