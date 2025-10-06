package com.tskim.portfolio.controller;

import com.tskim.portfolio.dto.common.ApiResponseDto;
import com.tskim.portfolio.dto.crypto.CryptoInfoDto;
import com.tskim.portfolio.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Crypto", description = "암호화폐 정보 조회 API")
public class CryptoController {
    
    private final CryptoService cryptoService;
    
    /**
     * 관심 암호화폐들의 실시간 정보 조회
     */
    @GetMapping
    @Operation(summary = "관심 암호화폐 실시간 정보 조회", description = "관심 암호화폐들의 실시간 가격 정보를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> getInterestedCryptosInfo() {
        try {
            log.info("관심 암호화폐 정보 조회 요청");
            
            List<CryptoInfoDto> cryptoInfos = cryptoService.getInterestedCryptosInfo();
            
            return ResponseEntity.ok(ApiResponseDto.success(cryptoInfos, "관심 암호화폐 정보 조회 성공"));
            
        } catch (Exception e) {
            log.error("관심 암호화폐 정보 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("암호화폐 정보 조회 중 오류가 발생했습니다"));
        }
    }
    
    /**
     * 특정 암호화폐들의 실시간 정보 조회
     */
    @GetMapping("/{cryptoCodes}")
    @Operation(summary = "특정 암호화폐 실시간 정보 조회", description = "암호화폐 코드들로 특정 암호화폐들의 실시간 정보를 조회합니다. 업비트 API와 동일한 형식으로 쉼표로 구분하여 여러 종목을 조회할 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 암호화폐 코드"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> getCryptoInfo(
            @Parameter(description = "암호화폐 코드들 (쉼표로 구분)", example = "KRW-BTC,KRW-ETH,KRW-XRP")
            @PathVariable("cryptoCodes") String cryptoCodes) {
        try {
            log.info("특정 암호화폐 정보 조회 요청: {}", cryptoCodes);
            
            // 암호화폐 코드 유효성 검증
            if (cryptoCodes == null || cryptoCodes.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDto.error("암호화폐 코드는 필수입니다"));
            }
            
            // 공백 제거 및 쉼표로 분리
            String[] codes = cryptoCodes.replaceAll("\\s+", "").split(",");
            
            // 각 코드 유효성 검증
            for (String code : codes) {
                if (!code.matches("^KRW-[A-Z]+$")) {
                    return ResponseEntity.badRequest()
                            .body(ApiResponseDto.error("암호화폐 코드는 KRW-XXX 형식이어야 합니다. 잘못된 코드: " + code));
                }
            }
            
            List<CryptoInfoDto> cryptoInfos = cryptoService.getCryptoInfos(codes);
            
            return ResponseEntity.ok(ApiResponseDto.success(cryptoInfos, "암호화폐 정보 조회 성공"));
            
        } catch (IllegalArgumentException e) {
            log.warn("암호화폐 정보 조회 실패 - 잘못된 암호화폐 코드: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
        } catch (Exception e) {
            log.error("암호화폐 정보 조회 중 오류 발생: cryptoCodes={}", cryptoCodes, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("암호화폐 정보 조회 중 오류가 발생했습니다"));
        }
    }
    
    /**
     * 목표가 알림 체크 (스케줄러에서 1분마다 호출)
     */
    @PostMapping("/check-target-price")
    @Operation(summary = "목표가 알림 체크", description = "관심 암호화폐들의 목표가 도달 여부를 체크하고 필요시 알림을 발송합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "체크 완료"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> checkTargetPriceNotifications() {
        try {
            log.info("암호화폐 목표가 알림 체크 API 호출");
            
            List<String> notifiedCryptos = cryptoService.checkTargetPriceNotifications();
            
            return ResponseEntity.ok(ApiResponseDto.success(notifiedCryptos, 
                "암호화폐 목표가 알림 체크 완료 - 알림 발송된 종목: " + notifiedCryptos.size() + "개"));
            
        } catch (Exception e) {
            log.error("암호화폐 목표가 알림 체크 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("암호화폐 목표가 알림 체크 중 오류가 발생했습니다"));
        }
    }
}
