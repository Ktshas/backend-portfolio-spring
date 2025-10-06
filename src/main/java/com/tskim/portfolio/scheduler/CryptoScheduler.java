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
    
}
