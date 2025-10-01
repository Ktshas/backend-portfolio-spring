package com.tskim.portfolio.service;

import com.tskim.portfolio.dto.StockInfoDto;
import com.tskim.portfolio.dto.StockResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    
    private final WebClient.Builder webClientBuilder;
    
    private static final String NAVER_STOCK_API_BASE_URL = "https://polling.finance.naver.com/api/realtime/domestic/stock";
    
    // 보유 주식 목록 (4종목)
    private static final List<String> HOLDING_STOCKS = List.of(
        "005935", // 삼성전자우
        "005930", // 삼성전자
        "000660", // SK하이닉스
        "005380"  // 현대차
    );
    
    // 주식별 목표가 매핑 (내 전용 매도, 매수 목표가이므로 하드코딩함)
    private static final Map<String, String> TARGET_PRICES = Map.of(
        "005935", "78,000", // 삼성전자우
        "005930", "98,000", // 삼성전자
        "000660", "290,000", // SK하이닉스
        "005380", "240,000"  // 현대차
    );
    
    /**
     * 보유 주식들의 실시간 정보를 조회합니다.
     * 
     * @return 보유 주식 정보 리스트
     */
    public List<StockInfoDto> getHoldingStocksInfo() {
        try {
            log.info("보유 주식 정보 조회 요청");
            
            List<StockInfoDto> stockInfos = HOLDING_STOCKS.stream()
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
                .targetPrice(TARGET_PRICES.get(stockData.getItemCode()))
                .build();
    }
}
