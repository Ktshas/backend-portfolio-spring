package com.tskim.portfolio.controller;

import com.tskim.portfolio.dto.common.ApiResponseDto;
import com.tskim.portfolio.dto.stock.StockInfoDto;
import com.tskim.portfolio.service.StockService;
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
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Stock", description = "주식 정보 조회 API")
public class StockController {
    
    private final StockService stockService;
    
    /**
     * 보유 주식들의 실시간 정보 조회
     */
    @GetMapping
    @Operation(summary = "보유 주식 실시간 정보 조회", description = "보유한 주식들의 실시간 주가 정보를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> getHoldingStocksInfo() {
        try {
            log.info("보유 주식 정보 조회 요청");
            
            List<StockInfoDto> stockInfos = stockService.getHoldingStocksInfo();
            
            return ResponseEntity.ok(ApiResponseDto.success(stockInfos, "보유 주식 정보 조회 성공"));
            
        } catch (Exception e) {
            log.error("보유 주식 정보 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("주식 정보 조회 중 오류가 발생했습니다"));
        }
    }
    
    /**
     * 특정 종목의 실시간 정보 조회
     */
    @GetMapping("/{itemCode}")
    @Operation(summary = "특정 종목 실시간 정보 조회", description = "종목 코드로 특정 주식의 실시간 정보를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 종목 코드"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> getStockInfo(
            @Parameter(description = "종목 코드", example = "005935")
            @PathVariable("itemCode") String itemCode) {
        try {
            log.info("특정 종목 정보 조회 요청: {}", itemCode);
            
            // 종목 코드 유효성 검증
            if (itemCode == null || itemCode.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDto.error("종목 코드는 필수입니다"));
            }
            
            if (!itemCode.matches("^\\d{6}$")) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDto.error("종목 코드는 6자리 숫자여야 합니다"));
            }
            
            StockInfoDto stockInfo = stockService.getStockInfo(itemCode);
            
            return ResponseEntity.ok(ApiResponseDto.success(stockInfo, "주식 정보 조회 성공"));
            
        } catch (IllegalArgumentException e) {
            log.warn("주식 정보 조회 실패 - 잘못된 종목 코드: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
        } catch (Exception e) {
            log.error("주식 정보 조회 중 오류 발생: itemCode={}", itemCode, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("주식 정보 조회 중 오류가 발생했습니다"));
        }
    }
    
    /**
     * 목표가 알림 체크 (Git Action에서 5분마다 호출)
     */
    @PostMapping("/check-target-price")
    @Operation(summary = "목표가 알림 체크", description = "보유 주식들의 목표가 도달 여부를 체크하고 필요시 알림을 발송합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "체크 완료"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> checkTargetPriceNotifications() {
        try {
            log.info("목표가 알림 체크 API 호출");
            
            List<String> notifiedStocks = stockService.checkTargetPriceNotifications();
            
            return ResponseEntity.ok(ApiResponseDto.success(notifiedStocks, 
                "목표가 알림 체크 완료 - 알림 발송된 종목: " + notifiedStocks.size() + "개"));
            
        } catch (Exception e) {
            log.error("목표가 알림 체크 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("목표가 알림 체크 중 오류가 발생했습니다"));
        }
    }
}
