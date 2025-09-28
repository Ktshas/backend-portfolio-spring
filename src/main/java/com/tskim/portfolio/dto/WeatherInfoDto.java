package com.tskim.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoDto {
    
    /**
     * 기온 (℃)
     */
    private Double temperature;
    
    /**
     * 1시간 강수량 (mm)
     */
    private String precipitation;
    
    /**
     * 하늘상태 (맑음: 1, 구름많음: 3, 흐림: 4)
     */
    private String skyCondition;
    
    /**
     * 강수형태 (없음: 0, 비: 1, 비/눈: 2, 눈: 3, 소나기: 4)
     */
    private String precipitationType;
    
    /**
     * 예측일자 (YYYYMMDD)
     */
    private String forecastDate;
    
    /**
     * 예측시간 (HHMM)
     */
    private String forecastTime;
    
    /**
     * 하늘상태를 한국어로 변환
     */
    public String getSkyConditionKorean() {
        if (skyCondition == null) return "정보없음";
        
        return switch (skyCondition) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "정보없음";
        };
    }
    
    /**
     * 강수형태를 한국어로 변환
     */
    public String getPrecipitationTypeKorean() {
        if (precipitationType == null) return "없음";
        
        return switch (precipitationType) {
            case "0" -> "없음";
            case "1" -> "비";
            case "2" -> "비/눈";
            case "3" -> "눈";
            case "4" -> "소나기";
            case "5" -> "빗방울";
            case "6" -> "빗방울눈날림";
            case "7" -> "눈날림";
            default -> "정보없음";
        };
    }
    
    /**
     * 강수량을 한국어로 변환
     */
    public String getPrecipitationKorean() {
        if (precipitation == null || precipitation.trim().isEmpty() || 
            precipitation.equals("0") || precipitation.equals("-")) {
            return "강수없음";
        }
        
        try {
            double value = Double.parseDouble(precipitation);
            if (value >= 0.1 && value < 1.0) {
                return "1mm 미만";
            } else if (value >= 1.0 && value < 30.0) {
                return String.format("%.1fmm", value);
            } else if (value >= 30.0 && value < 50.0) {
                return "30.0~50.0mm";
            } else if (value >= 50.0) {
                return "50.0mm 이상";
            }
        } catch (NumberFormatException e) {
            // 파싱 실패 시 원본 값 반환
        }
        
        return precipitation + "mm";
    }
}
