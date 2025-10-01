package com.tskim.portfolio.service;

import com.tskim.portfolio.constants.StockConstants;
import com.tskim.portfolio.dto.stock.StockInfoDto;
import com.tskim.portfolio.dto.stock.StockResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    
    private final WebClient.Builder webClientBuilder;
    
    private static final String NAVER_STOCK_API_BASE_URL = "https://polling.finance.naver.com/api/realtime/domestic/stock";
    
    
    /**
     * 보유 주식들의 실시간 정보를 조회합니다.
     * 
     * @return 보유 주식 정보 리스트
     */
    public List<StockInfoDto> getHoldingStocksInfo() {
        try {
            log.info("보유 주식 정보 조회 요청");
            
            List<StockInfoDto> stockInfos = StockConstants.HOLDING_STOCKS.stream()
                    .map(this::getStockInfo)
                    .toList();
            
            log.info("보유 주식 정보 조회 완료: {} 종목", stockInfos.size());
            return stockInfos;
            
        } catch (Exception e) {
            log.error("보유 주식 정보 조회 중 오류 발생", e);
            throw new RuntimeException("주식 정보 조회 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 특정 종목의 실시간 정보를 조회합니다.
     * 
     * @param itemCode 종목 코드
     * @return 주식 정보
     */
    public StockInfoDto getStockInfo(String itemCode) {
        try {
            log.info("주식 정보 조회 요청: itemCode={}", itemCode);
            
            // 네이버 주식 API 호출
            StockResponseDto response = callNaverStockApi(itemCode);
            
            // 응답 데이터 파싱
            StockInfoDto stockInfo = parseStockResponse(response);
            
            log.info("주식 정보 조회 완료: {}", stockInfo);
            return stockInfo;
            
        } catch (Exception e) {
            log.error("주식 정보 조회 중 오류 발생: itemCode={}", itemCode, e);
            throw new RuntimeException("주식 정보 조회 중 오류가 발생했습니다: " + itemCode, e);
        }
    }
    
    /**
     * 네이버 주식 API를 호출합니다.
     */
    private StockResponseDto callNaverStockApi(String itemCode) {
        String url = NAVER_STOCK_API_BASE_URL + "/" + itemCode;
        
        log.debug("네이버 주식 API 호출 URL: {}", url);
        
        WebClient webClient = webClientBuilder.build();
        
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(StockResponseDto.class)
                .timeout(Duration.ofSeconds(10))
                .block();
    }
    
    /**
     * 네이버 주식 API 응답을 파싱하여 StockInfoDto로 변환합니다.
     */
    private StockInfoDto parseStockResponse(StockResponseDto response) {
        if (response == null || response.getDatas() == null || response.getDatas().isEmpty()) {
            throw new RuntimeException("네이버 주식 API 응답이 올바르지 않습니다.");
        }
        
        StockResponseDto.StockData stockData = response.getDatas().get(0);
        
        return StockInfoDto.builder()
                .itemCode(stockData.getItemCode())
                .stockName(stockData.getStockName())
                .closePrice(stockData.getClosePrice())
                .compareToPreviousClosePrice(stockData.getCompareToPreviousClosePrice())
                .fluctuationsRatio(stockData.getFluctuationsRatio())
                .compareDirection(stockData.getCompareToPreviousPrice() != null ? 
                    stockData.getCompareToPreviousPrice().getName() : null)
                .openPrice(stockData.getOpenPrice())
                .highPrice(stockData.getHighPrice())
                .lowPrice(stockData.getLowPrice())
                .accumulatedTradingVolume(stockData.getAccumulatedTradingVolume())
                .accumulatedTradingValue(stockData.getAccumulatedTradingValue())
                .marketStatus(stockData.getMarketStatus())
                .localTradedAt(stockData.getLocalTradedAt())
                .currencyType(stockData.getCurrencyType() != null ? 
                    stockData.getCurrencyType().getCode() : null)
                .targetPrice(StockConstants.getTargetPrice(stockData.getItemCode()))
                .targetPriceDirection(StockConstants.getTargetPriceDirection(stockData.getItemCode()) != null ? 
                    StockConstants.getTargetPriceDirection(stockData.getItemCode()).name() : null)
                .build();
    }
    
    /**
     * 보유 주식들의 목표가 알림을 체크합니다.
     * Git Action에서 5분마다 호출하여 목표가 도달 시 카카오톡 알림을 보냅니다.
     * 
     * @return 알림이 발송된 주식 목록
     */
    public List<String> checkTargetPriceNotifications() {
        try {
            log.info("목표가 알림 체크 시작");
            
            List<String> notifiedStocks = StockConstants.HOLDING_STOCKS.stream()
                    .filter(this::checkAndSendNotification)
                    .toList();
            
            if (!notifiedStocks.isEmpty()) {
                log.info("목표가 알림 발송 완료: {} 종목", notifiedStocks);
            } else {
                log.debug("목표가 도달한 주식 없음");
            }
            
            return notifiedStocks;
            
        } catch (Exception e) {
            log.error("목표가 알림 체크 중 오류 발생", e);
            throw new RuntimeException("목표가 알림 체크 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 특정 주식의 목표가 알림을 체크하고 필요시 알림을 발송합니다.
     * 
     * @param itemCode 종목 코드
     * @return 알림 발송 여부
     */
    private boolean checkAndSendNotification(String itemCode) {
        try {
            StockInfoDto stockInfo = getStockInfo(itemCode);
            
            // 목표가 알림 조건 체크
            if (StockConstants.shouldSendNotification(itemCode, stockInfo.getClosePrice())) {
                sendKakaoNotification(stockInfo);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            log.error("주식 {} 목표가 알림 체크 중 오류 발생", itemCode, e);
            return false;
        }
    }
    
    /**
     * 카카오톡 알림을 발송합니다.
     * TODO: 카카오톡 API 연동 구현 필요
     * 
     * @param stockInfo 주식 정보
     */
    private void sendKakaoNotification(StockInfoDto stockInfo) {
        try {
            String stockName = StockConstants.getStockName(stockInfo.getItemCode());
            String targetPrice = stockInfo.getTargetPrice();
            String currentPrice = stockInfo.getClosePrice();
            String direction = StockConstants.getTargetPriceDirection(stockInfo.getItemCode()).name();
            
            // 알림 메시지 생성 (방향에 따라 다른 메시지)
            String directionKorean = direction.equals("UP") ? "이상" : "이하";
            String emoji = direction.equals("UP") ? "📈" : "📉";
            
            String message = String.format(
                "%s 주식 알림\n" +
                "종목: %s (%s)\n" +
                "현재가: %s원\n" +
                "목표가: %s원 (%s 방향)\n" +
                "목표가 도달! 🎯",
                emoji, stockName, stockInfo.getItemCode(), 
                currentPrice, targetPrice, directionKorean
            );
            
            log.info("카카오톡 알림 발송: {}", message);
            
            // TODO: 카카오톡 API 연동 구현
            // 1. 카카오톡 API 토큰 발급
            // 2. 메시지 전송 API 호출
            // 3. 발송 결과 로깅 및 예외 처리
            
            // 임시로 로그만 출력 (실제 구현 시 제거)
            log.info("=== 카카오톡 알림 메시지 ===");
            log.info("{}", message);
            log.info("==========================");
            
        } catch (Exception e) {
            log.error("카카오톡 알림 발송 중 오류 발생", e);
        }
    }
}
