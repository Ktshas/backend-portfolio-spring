package com.tskim.portfolio.constants;

import java.util.List;
import java.util.Map;

/**
 * 주식 관련 상수 정의 클래스
 * 보유 주식 목록, 목표가, 알림 설정 등을 관리
 */
public class StockConstants {
    private class StockCode {
        public static final String SAMSUNG_ELEC = "005930";
        public static final String SAMSUNG_ELEC_SUB = "005935";
        public static final String SK_HYNIX = "000660";
        public static final String HYUNDAI_MOTOR = "005380";
    }
    
    // 보유 주식 목록 (4종목)
    public static final List<String> HOLDING_STOCKS = List.of(
        StockCode.SAMSUNG_ELEC, // 삼성전자
        StockCode.SAMSUNG_ELEC_SUB, // 삼성전자우
        StockCode.SK_HYNIX, // SK하이닉스
        StockCode.HYUNDAI_MOTOR  // 현대차
    );
    
    // 주식별 목표가 매핑 (내 전용 매도, 매수 목표가이므로 하드코딩함)
    public static final Map<String, String> TARGET_PRICES = Map.of(
        StockCode.SAMSUNG_ELEC, "98,000", // 삼성전자
        StockCode.SAMSUNG_ELEC_SUB, "78,000", // 삼성전자
        StockCode.SK_HYNIX, "290,000", // SK하이닉스
        StockCode.HYUNDAI_MOTOR, "240,000"  // 현대차
    );
    
    // 주식별 목표가 알림 방향 설정 (UP: 목표가 이상일 때 알림, DOWN: 목표가 이하일 때 알림)
    public static final Map<String, TargetPriceDirection> TARGET_PRICE_DIRECTIONS = Map.of(
        StockCode.SAMSUNG_ELEC, TargetPriceDirection.UP,   // 삼성전자 - 98,000원 이상일 때 알림
        StockCode.SAMSUNG_ELEC_SUB, TargetPriceDirection.UP,   // 삼성전자우 - 78,000원 이상일 때 알림
        StockCode.SK_HYNIX, TargetPriceDirection.DOWN,   // SK하이닉스 - 290,000원 이하일 때 알림
        StockCode.HYUNDAI_MOTOR, TargetPriceDirection.UP    // 현대차 - 240,000원 이상일 때 알림
    );
    
    // 주식명 매핑
    public static final Map<String, String> STOCK_NAMES = Map.of(
        StockCode.SAMSUNG_ELEC, "삼성전자",
        StockCode.SAMSUNG_ELEC_SUB, "삼성전자우",
        StockCode.SK_HYNIX, "SK하이닉스",
        StockCode.HYUNDAI_MOTOR, "현대차"
    );
    
    /**
     * 목표가 알림 방향 열거형
     */
    public enum TargetPriceDirection {
        UP,     // 목표가 이상일 때 알림
        DOWN    // 목표가 이하일 때 알림
    }
    
    /**
     * 주식 코드로 목표가를 조회합니다.
     * 
     * @param itemCode 종목 코드
     * @return 목표가 (문자열)
     */
    public static String getTargetPrice(String itemCode) {
        return TARGET_PRICES.get(itemCode);
    }
    
    /**
     * 주식 코드로 알림 방향을 조회합니다.
     * 
     * @param itemCode 종목 코드
     * @return 알림 방향
     */
    public static TargetPriceDirection getTargetPriceDirection(String itemCode) {
        return TARGET_PRICE_DIRECTIONS.get(itemCode);
    }
    
    /**
     * 주식 코드로 주식명을 조회합니다.
     * 
     * @param itemCode 종목 코드
     * @return 주식명
     */
    public static String getStockName(String itemCode) {
        return STOCK_NAMES.get(itemCode);
    }
    
    /**
     * 현재가와 목표가를 비교하여 알림이 필요한지 확인합니다.
     * 
     * @param itemCode 종목 코드
     * @param currentPrice 현재가 (문자열, 쉼표 포함)
     * @return 알림 필요 여부
     */
    public static boolean shouldSendNotification(String itemCode, String currentPrice) {
        String targetPriceStr = getTargetPrice(itemCode);
        TargetPriceDirection direction = getTargetPriceDirection(itemCode);
        
        if (targetPriceStr == null || direction == null) {
            return false;
        }
        
        try {
            // 쉼표 제거 후 숫자로 변환
            int current = Integer.parseInt(currentPrice.replace(",", ""));
            int target = Integer.parseInt(targetPriceStr.replace(",", ""));
            
            return switch (direction) {
                case UP -> current >= target;
                case DOWN -> current <= target;
            };
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
