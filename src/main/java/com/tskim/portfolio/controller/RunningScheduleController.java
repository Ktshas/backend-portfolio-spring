package com.tskim.portfolio.controller;

import com.tskim.portfolio.dto.common.ApiResponseDto;
import com.tskim.portfolio.dto.running.RunningScheduleDto;
import com.tskim.portfolio.service.RunningScheduleService;
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

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/schedules/running")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Running Schedule", description = "러닝 스케줄 관리 API")
public class RunningScheduleController {
    
    private final RunningScheduleService runningScheduleService;
    
    /**
     * 러닝 스케줄 등록
     */
    @PostMapping
    @Operation(summary = "러닝 스케줄 등록", description = "새로운 러닝 스케줄을 등록합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "스케줄 등록 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> createSchedule(@Valid @RequestBody RunningScheduleDto scheduleDto) {
        try {
            log.info("러닝 스케줄 등록 요청: {}", scheduleDto.getTitle());
            
            RunningScheduleDto createdSchedule = runningScheduleService.createSchedule(scheduleDto);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(createdSchedule, "러닝 스케줄 등록 성공"));
            
        } catch (IllegalArgumentException e) {
            log.warn("러닝 스케줄 등록 실패 - 검증 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponseDto.error(e.getMessage()));
        } catch (Exception e) {
            log.error("러닝 스케줄 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error("서버 내부 오류가 발생했습니다"));
        }
    }
    
    /**
     * 특정 년월의 러닝 스케줄 조회
     */
    @GetMapping
    @Operation(summary = "특정 년월 러닝 스케줄 조회", description = "지정된 년월의 러닝 스케줄을 날짜순으로 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 년월 형식"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> getSchedulesByMonth(
            @Parameter(description = "조회할 년월 (YYYYMM 형식)", example = "202409")
            @RequestParam("yearMonth") String yearMonth) {
        try {
            log.info("특정 년월 러닝 스케줄 조회 요청: {}", yearMonth);
            
            // 년월 형식 유효성 검증
            if (yearMonth == null || !yearMonth.matches("^\\d{6}$")) {
                return ResponseEntity.badRequest().body(ApiResponseDto.error("년월은 YYYYMM 형식이어야 합니다 (예: 202409)"));
            }
            
            // 년월 값 유효성 검증
            int year = Integer.parseInt(yearMonth.substring(0, 4));
            int month = Integer.parseInt(yearMonth.substring(4, 6));
            
            if (year < 2000 || year > 2100) {
                return ResponseEntity.badRequest().body(ApiResponseDto.error("년도는 2000년부터 2100년 사이여야 합니다"));
            }
            
            if (month < 1 || month > 12) {
                return ResponseEntity.badRequest().body(ApiResponseDto.error("월은 1부터 12 사이여야 합니다"));
            }
            
            List<RunningScheduleDto> schedules = runningScheduleService.getSchedulesByMonth(yearMonth);
            
            return ResponseEntity.ok(ApiResponseDto.success(schedules, "월별 러닝 스케줄 조회 성공"));
            
        } catch (NumberFormatException e) {
            log.warn("년월 파싱 실패: {}", yearMonth);
            return ResponseEntity.badRequest().body(ApiResponseDto.error("년월은 YYYYMM 형식이어야 합니다 (예: 202409)"));
        } catch (Exception e) {
            log.error("특정 년월 러닝 스케줄 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error("서버 내부 오류가 발생했습니다"));
        }
    }
    
    /**
     * 특정 날짜의 러닝 스케줄 조회
     */
    @GetMapping("/date/{date}")
    @Operation(summary = "날짜별 러닝 스케줄 조회", description = "특정 날짜의 러닝 스케줄을 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> getSchedulesByDate(
            @Parameter(description = "조회할 날짜 (YYYYMMDD)", example = "20240925")
            @PathVariable("date") String date) {
        try {
            log.info("날짜별 러닝 스케줄 조회 요청: {}", date);
            
            List<RunningScheduleDto> schedules = runningScheduleService.getSchedulesByDate(date);
            
            return ResponseEntity.ok(ApiResponseDto.success(schedules, "날짜별 러닝 스케줄 조회 성공"));
            
        } catch (Exception e) {
            log.error("날짜별 러닝 스케줄 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error("서버 내부 오류가 발생했습니다"));
        }
    }
    
    /**
     * ID로 러닝 스케줄 조회
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID로 러닝 스케줄 조회", description = "MongoDB ID로 특정 러닝 스케줄을 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "해당 ID의 스케줄을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> getScheduleById(
            @Parameter(description = "조회할 스케줄의 MongoDB ID", example = "507f1f77bcf86cd799439011")
            @PathVariable("id") String id) {
        try {
            log.info("ID로 러닝 스케줄 조회 요청: {}", id);
            
            RunningScheduleDto schedule = runningScheduleService.getScheduleById(id);
            
            return ResponseEntity.ok(ApiResponseDto.success(schedule, "러닝 스케줄 조회 성공"));
            
        } catch (IllegalArgumentException e) {
            log.warn("ID로 러닝 스케줄 조회 실패 - 스케줄을 찾을 수 없음: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseDto.error(e.getMessage()));
        } catch (Exception e) {
            log.error("ID로 러닝 스케줄 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error("서버 내부 오류가 발생했습니다"));
        }
    }
    
    /**
     * 날짜 범위별 러닝 스케줄 조회
     */
    @GetMapping("/range")
    @Operation(summary = "날짜 범위별 러닝 스케줄 조회", description = "시작일과 종료일 사이의 러닝 스케줄을 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> getSchedulesByDateRange(
            @Parameter(description = "시작 날짜 (YYYYMMDD)", example = "20240901")
            @RequestParam("startDate") String startDate,
            @Parameter(description = "종료 날짜 (YYYYMMDD)", example = "20240930")
            @RequestParam("endDate") String endDate) {
        try {
            log.info("날짜 범위별 러닝 스케줄 조회 요청: {} ~ {}", startDate, endDate);
            
            if (endDate.compareTo(startDate) < 0) {
                return ResponseEntity.badRequest().body(ApiResponseDto.error("종료일은 시작일보다 늦어야 합니다"));
            }
            
            List<RunningScheduleDto> schedules = runningScheduleService.getSchedulesByDateRange(startDate, endDate);
            
            return ResponseEntity.ok(ApiResponseDto.success(schedules, "날짜 범위별 러닝 스케줄 조회 성공"));
            
        } catch (Exception e) {
            log.error("날짜 범위별 러닝 스케줄 조회 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseDto.error("서버 내부 오류가 발생했습니다"));
        }
    }
    
}
