package com.tskim.portfolio.dto.running;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tskim.portfolio.entity.RunningSchedule;
import com.tskim.portfolio.dto.weather.WeatherInfoDto;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class RunningScheduleDto {
    private String id;
    
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다")
    private String title;
    
    @NotBlank(message = "날짜는 필수입니다")
    @Pattern(regexp = "^\\d{8}$", message = "날짜 형식은 YYYYMMDD이어야 합니다 (예: 20250926)")
    private String date;
    
    @NotBlank(message = "시작시간은 필수입니다")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3])[0-5][0-9]$", message = "시간 형식은 HHMM이어야 합니다 (예: 1730)")
    private String startTime;
    
    @NotBlank(message = "종료시간은 필수입니다")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3])[0-5][0-9]$", message = "시간 형식은 HHMM이어야 합니다 (예: 1200)")
    private String endTime;
    
    // 이하 카카오 로컬-키워드 장소 검색 api response에 맞춘 변수
    @NotNull(message = "X 좌표는 필수입니다")
    private Integer x;
    
    @NotNull(message = "Y 좌표는 필수입니다")
    private Integer y;
    
    @NotBlank(message = "장소는 필수입니다")
    @Size(max = 100, message = "장소는 100자를 초과할 수 없습니다")
    private String placeName;
    
    @Size(max = 50, message = "상세장소는 50자를 초과할 수 없습니다")
    private String placeDetail;

    private String placeUrl;

    private String addressName;
    
    // 날씨 정보 (API 응답 시에만 포함)
    private WeatherInfoDto weatherInfo;
    
    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
    
    public static RunningScheduleDto from(RunningSchedule schedule) {
        RunningScheduleDto dto = new RunningScheduleDto();
        dto.setId(schedule.getId());
        dto.setTitle(schedule.getTitle());
        dto.setDate(schedule.getDate());           // 기상청 API 형식 그대로
        dto.setStartTime(schedule.getStartTime()); // 기상청 API 형식 그대로
        dto.setEndTime(schedule.getEndTime());     // 기상청 API 형식 그대로
        dto.setX(schedule.getX());                 // 기상청 API 형식 그대로
        dto.setY(schedule.getY());                 // 기상청 API 형식 그대로
        dto.setPlaceName(schedule.getPlaceName());
        dto.setAddressName(schedule.getAddressName());
        dto.setPlaceDetail(schedule.getPlaceDetail());
        dto.setPlaceUrl(schedule.getPlaceUrl());
        dto.setCreatedAt(schedule.getCreatedAt());
        dto.setUpdatedAt(schedule.getUpdatedAt());
        return dto;
    }
    
    public RunningSchedule toEntity() {
        RunningSchedule schedule = new RunningSchedule();
        schedule.setTitle(this.title);
        schedule.setDate(this.date);           // 기상청 API 형식 그대로 저장
        schedule.setYearMonth(extractYearMonth(this.date)); // 년월 자동 계산
        schedule.setStartTime(this.startTime); // 기상청 API 형식 그대로 저장
        schedule.setEndTime(this.endTime);     // 기상청 API 형식 그대로 저장
        schedule.setX(this.x);                 // 기상청 API 형식 그대로 저장
        schedule.setY(this.y);                 // 기상청 API 형식 그대로 저장
        schedule.setAddressName(this.addressName);
        schedule.setPlaceDetail(this.placeDetail);
        schedule.setPlaceUrl(this.placeUrl);
        schedule.setPlaceName(this.placeName);
        return schedule;
    }
    
    /**
     * YYYYMMDD 형식의 날짜에서 YYYYMM 형식의 년월을 추출
     */
    private String extractYearMonth(String date) {
        if (date == null || date.length() < 6) {
            return null;
        }
        return date.substring(0, 6); // YYYYMMDD -> YYYYMM
    }
}
