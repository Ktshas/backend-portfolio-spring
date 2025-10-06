package com.tskim.portfolio.constants;

import java.util.List;
import java.util.Map;

/**
 * 암호화폐 관련 상수 정의 클래스
 * 관심 암호화폐 목록, 목표가, 알림 설정 등을 관리
 */
public class CryptoConstants {
    
    private class CryptoCode {
        public static final String BTC = "KRW-BTC";
        public static final String ETH = "KRW-ETH";
        public static final String XRP = "KRW-XRP";
    }
    
    // 관심 암호화폐 목록 (쉼표로 구분된 문자열)
    public static final String INTERESTED_CRYPTOS = CryptoCode.BTC+","+CryptoCode.ETH+","+CryptoCode.XRP;
    
    // 목표가 알림 대상 암호화폐 (BTC만)
    public static final List<String> TARGET_PRICE_ALERT_CRYPTOS = List.of(
        CryptoCode.BTC     // 비트코인만 목표가 알림
    );
    
    // 암호화폐별 목표가 매핑 (내 전용 매도, 매수 목표가이므로 하드코딩함)
    public static final Map<String, String> TARGET_PRICES = Map.of(
        CryptoCode.BTC, "155,000,000"    // 비트코인 - 1억 5천 5백만원
    );
    
    // 암호화폐별 목표가 알림 방향 설정 (UP: 목표가 이상일 때 알림, DOWN: 목표가 이하일 때 알림)
    public static final Map<String, TargetPriceDirection> TARGET_PRICE_DIRECTIONS = Map.of(
        CryptoCode.BTC, TargetPriceDirection.DOWN    // 비트코인 - 1억 5천 5백만원 이하일 때 알림
    );
    
    // 암호화폐명 매핑
    public static final Map<String, String> CRYPTO_NAMES = Map.of(
        CryptoCode.BTC, "비트코인",
        CryptoCode.ETH, "이더리움",
        CryptoCode.XRP, "리플"
    );
    
    // 업비트 API URL
    public static final String UPBIT_API_BASE_URL = "https://api.upbit.com/v1";
    public static final String UPBIT_TICKER_URL = UPBIT_API_BASE_URL + "/ticker";
    
    /**
     * 목표가 알림 방향 열거형
     */
    public enum TargetPriceDirection {
        UP,     // 목표가 이상일 때 알림
        DOWN    // 목표가 이하일 때 알림
    }
    
    /**
     * 암호화폐 코드로 목표가를 조회합니다.
     * 
     * @param cryptoCode 암호화폐 코드
     * @return 목표가 (문자열)
     */
    public static String getTargetPrice(String cryptoCode) {
        return TARGET_PRICES.get(cryptoCode);
    }
    
    /**
     * 암호화폐 코드로 알림 방향을 조회합니다.
     * 
     * @param cryptoCode 암호화폐 코드
     * @return 알림 방향
     */
    public static TargetPriceDirection getTargetPriceDirection(String cryptoCode) {
        return TARGET_PRICE_DIRECTIONS.get(cryptoCode);
    }
    
    /**
     * 암호화폐 코드로 암호화폐명을 조회합니다.
     * 
     * @param cryptoCode 암호화폐 코드
     * @return 암호화폐명
     */
    public static String getCryptoName(String cryptoCode) {
        return CRYPTO_NAMES.get(cryptoCode);
    }
    
    /**
     * 현재가와 목표가를 비교하여 알림이 필요한지 확인합니다.
     * 
     * @param cryptoCode 암호화폐 코드
     * @param currentPrice 현재가 (문자열, 쉼표 포함)
     * @return 알림 필요 여부
     */
    public static boolean shouldSendNotification(String cryptoCode, String currentPrice) {
        String targetPriceStr = getTargetPrice(cryptoCode);
        TargetPriceDirection direction = getTargetPriceDirection(cryptoCode);
        
        if (targetPriceStr == null || direction == null) {
            return false;
        }
        
        try {
            // 쉼표 제거 후 숫자로 변환
            long current = Long.parseLong(currentPrice.replace(",", ""));
            long target = Long.parseLong(targetPriceStr.replace(",", ""));
            
            return switch (direction) {
                case UP -> current >= target;
                case DOWN -> current <= target;
            };
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
}
