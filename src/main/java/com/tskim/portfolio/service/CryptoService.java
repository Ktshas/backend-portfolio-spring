package com.tskim.portfolio.service;

import com.tskim.portfolio.constants.CryptoConstants;
import com.tskim.portfolio.dto.crypto.CryptoInfoDto;
import com.tskim.portfolio.dto.crypto.CryptoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoService {
    
    private final WebClient.Builder webClientBuilder;
    
    /**
     * 암호화폐들의 실시간 정보를 조회합니다.
     * 
     * @param cryptoCodes 암호화폐 코드들 (쉼표로 구분, 예: "KRW-BTC,KRW-ETH,KRW-XRP")
     * @return 암호화폐 정보 리스트
     */
    public List<CryptoInfoDto> getCryptoInfos(String cryptoCodes) {
        try {
            log.info("암호화폐 정보 조회 요청: {}", cryptoCodes);
            
            // 암호화폐 코드 유효성 검증
            if (cryptoCodes == null || cryptoCodes.trim().isEmpty()) {
                throw new IllegalArgumentException("암호화폐 코드는 필수입니다");
            }
            
            // 각 코드 유효성 검증
            String[] codes = cryptoCodes.split(",");
            for (String code : codes) {
                if (code == null || code.trim().isEmpty()) {
                    throw new IllegalArgumentException("암호화폐 코드는 비어있을 수 없습니다");
                }
                if (!code.matches("^KRW-[A-Z]+$")) {
                    throw new IllegalArgumentException("암호화폐 코드는 KRW-XXX 형식이어야 합니다. 잘못된 코드: " + code);
                }
            }
            
            // 업비트 API 호출
            List<CryptoResponseDto> cryptoResponses = callUpbitApi(cryptoCodes);
            
            if (cryptoResponses.isEmpty()) {
                throw new IllegalArgumentException("해당 암호화폐 정보를 찾을 수 없습니다: " + cryptoCodes);
            }
            
            // 응답 데이터 파싱
            List<CryptoInfoDto> cryptoInfos = cryptoResponses.stream()
                    .map(this::parseCryptoResponse)
                    .toList();
            
            log.info("암호화폐 정보 조회 완료: {} 종목", cryptoInfos.size());
            return cryptoInfos;
            
        } catch (IllegalArgumentException e) {
            log.warn("암호화폐 정보 조회 실패 - 잘못된 요청: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("암호화폐 정보 조회 중 오류 발생: cryptoCodes={}", cryptoCodes, e);
            throw new RuntimeException("암호화폐 정보 조회 중 오류가 발생했습니다: " + cryptoCodes, e);
        }
    }
    
    /**
     * 업비트 API를 호출합니다.
     * 
     * @param markets 조회할 암호화폐 코드들 (쉼표로 구분)
     * @return 암호화폐 응답 리스트
     */
    private List<CryptoResponseDto> callUpbitApi(String markets) {
        String url = CryptoConstants.UPBIT_TICKER_URL + "?markets=" + markets;
        
        log.debug("업비트 API 호출 URL: {}", url);
        
        WebClient webClient = webClientBuilder.build();
        
        return webClient.get()
                .uri(url)
                .header("accept", "application/json")
                .retrieve()
                .bodyToFlux(CryptoResponseDto.class)
                .collectList()
                .timeout(Duration.ofSeconds(10))
                .block();
    }
    
    /**
     * 업비트 API 응답을 파싱하여 CryptoInfoDto로 변환합니다.
     */
    private CryptoInfoDto parseCryptoResponse(CryptoResponseDto response) {
        if (response == null) {
            throw new RuntimeException("업비트 API 응답이 올바르지 않습니다.");
        }
        
        return CryptoInfoDto.builder()
                .cryptoCode(response.getMarket())
                .cryptoName(CryptoConstants.getCryptoName(response.getMarket()))
                .tradePrice(response.getTradePrice())
                .changePrice(response.getChangePrice())
                .changeRate(String.format("%.2f", Double.parseDouble(response.getChangeRate()) * 100))
                .change(response.getChange())
                .openingPrice(response.getOpeningPrice())
                .highPrice(response.getHighPrice())
                .lowPrice(response.getLowPrice())
                .tradeVolume(response.getTradeVolume())
                .accTradePrice(response.getAccTradePrice())
                .accTradePrice24h(response.getAccTradePrice24h())
                .accTradeVolume24h(response.getAccTradeVolume24h())
                .highest52WeekPrice(response.getHighest52WeekPrice())
                .lowest52WeekPrice(response.getLowest52WeekPrice())
                .tradeTimeKst(response.getTradeTimeKst())
                .targetPrice(CryptoConstants.getTargetPrice(response.getMarket()))
                .targetPriceDirection(CryptoConstants.getTargetPriceDirection(response.getMarket()) != null ? 
                    CryptoConstants.getTargetPriceDirection(response.getMarket()).name() : null)
                .build();
    }
    
    /**
     * 목표가 알림 대상 암호화폐들의 목표가 알림을 체크합니다.
     * 스케줄러에서 1분마다 호출하여 목표가 도달 시 알림을 보냅니다.
     * 
     * @return 알림이 발송된 암호화폐 목록
     */
    public List<String> checkTargetPriceNotifications() {
        try {
            log.info("암호화폐 목표가 알림 체크 시작");
            
            // 목표가 알림 대상 암호화폐들만 조회
            String targetCryptoCodes = String.join(",", CryptoConstants.TARGET_PRICE_ALERT_CRYPTOS);
            List<CryptoInfoDto> targetCryptoInfos = getCryptoInfos(targetCryptoCodes);
            
            List<String> notifiedCryptos = targetCryptoInfos.stream()
                    .filter(this::checkAndSendNotification)
                    .map(CryptoInfoDto::getCryptoCode)
                    .toList();
            
            if (!notifiedCryptos.isEmpty()) {
                log.info("암호화폐 목표가 알림 발송 완료: {} 종목", notifiedCryptos);
            } else {
                log.debug("목표가 도달한 암호화폐 없음");
            }
            
            return notifiedCryptos;
            
        } catch (Exception e) {
            log.error("암호화폐 목표가 알림 체크 중 오류 발생", e);
            throw new RuntimeException("암호화폐 목표가 알림 체크 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 특정 암호화폐의 목표가 알림을 체크하고 필요시 알림을 발송합니다.
     * 
     * @param cryptoInfo 암호화폐 정보
     * @return 알림 발송 여부
     */
    private boolean checkAndSendNotification(CryptoInfoDto cryptoInfo) {
        try {
            // 목표가 알림 조건 체크
            if (CryptoConstants.shouldSendNotification(cryptoInfo.getCryptoCode(), cryptoInfo.getTradePrice())) {
                sendNotification(cryptoInfo);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            log.error("암호화폐 {} 목표가 알림 체크 중 오류 발생", cryptoInfo.getCryptoCode(), e);
            return false;
        }
    }
    
    /**
     * 알림을 발송합니다.
     * TODO: 카카오톡 API 연동 구현 필요
     * 
     * @param cryptoInfo 암호화폐 정보
     */
    private void sendNotification(CryptoInfoDto cryptoInfo) {
        try {
            String cryptoName = cryptoInfo.getCryptoName();
            String targetPrice = cryptoInfo.getTargetPrice();
            String currentPrice = cryptoInfo.getTradePrice();
            String direction = CryptoConstants.getTargetPriceDirection(cryptoInfo.getCryptoCode()).name();
            
            // 알림 메시지 생성 (방향에 따라 다른 메시지)
            String directionKorean = direction.equals("UP") ? "이상" : "이하";
            String emoji = direction.equals("UP") ? "🚀" : "📉";
            
            String message = String.format(
                "%s 암호화폐 알림\n" +
                "종목: %s (%s)\n" +
                "현재가: %s원\n" +
                "목표가: %s원 (%s 방향)\n" +
                "목표가 도달! 🎯",
                emoji, cryptoName, cryptoInfo.getCryptoCode(), 
                currentPrice, targetPrice, directionKorean
            );
            
            log.info("암호화폐 알림 발송: {}", message);
            
            // TODO: 카카오톡 API 연동 구현
            // 1. 카카오톡 API 토큰 발급
            // 2. 메시지 전송 API 호출
            // 3. 발송 결과 로깅 및 예외 처리
            
            // 임시로 로그만 출력 (실제 구현 시 제거)
            log.info("=== 암호화폐 알림 메시지 ===");
            log.info("{}", message);
            log.info("==========================");
            
        } catch (Exception e) {
            log.error("암호화폐 알림 발송 중 오류 발생", e);
        }
    }
}
