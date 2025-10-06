package com.tskim.portfolio.scheduler;

import com.tskim.portfolio.service.CryptoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CryptoScheduler {
    
    private final CryptoService cryptoService;
    
    /**
     * 암호화폐 목표가 알림 체크 스케줄러
     * 1분마다 실행되어 목표가 도달 시 알림을 발송합니다.
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행 (60초 = 60000ms)
    public void checkCryptoTargetPriceNotifications() {
        try {
            log.debug("암호화폐 목표가 알림 체크 스케줄러 실행");
            
            List<String> notifiedCryptos = cryptoService.checkTargetPriceNotifications();
            
            if (!notifiedCryptos.isEmpty()) {
                log.info("스케줄러를 통해 암호화폐 목표가 알림 발송: {}", notifiedCryptos);
            }
            
        } catch (Exception e) {
            log.error("암호화폐 목표가 알림 체크 스케줄러 실행 중 오류 발생", e);
        }
    }
    
    /**
     * 암호화폐 정보 수집 스케줄러 (선택적)
     * 1분마다 실행되어 암호화폐 정보를 수집하고 로깅합니다.
     * 실제 운영에서는 필요에 따라 활성화/비활성화할 수 있습니다.
     */
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void collectCryptoInfo() {
        try {
            log.debug("암호화폐 정보 수집 스케줄러 실행");
            
            // 관심 암호화폐 정보 조회
            var cryptoInfos = cryptoService.getInterestedCryptosInfo();
            
            // 목표가 알림 대상 암호화폐들의 현재가 로깅 (BTC만)
            cryptoInfos.stream()
                    .filter(crypto -> crypto.getCryptoCode().equals("KRW-BTC"))
                    .forEach(crypto -> log.info("암호화폐 정보 수집 - {}: {}원 ({}%) - 목표가: {}원", 
                            crypto.getCryptoName(), 
                            crypto.getTradePrice(), 
                            crypto.getChangeRate(),
                            crypto.getTargetPrice()));
            
        } catch (Exception e) {
            log.error("암호화폐 정보 수집 스케줄러 실행 중 오류 발생", e);
        }
    }
}
