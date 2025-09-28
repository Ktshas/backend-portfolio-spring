package com.tskim.portfolio.service;

import com.tskim.portfolio.dto.WeatherInfoDto;
import com.tskim.portfolio.dto.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${weather.api.base-url}")
    private String baseUrl;
    
    @Value("${weather.api.service-key}")
    private String serviceKey;
    
    @Value("${weather.api.num-of-rows}")
    private int numOfRows;
    
    @Value("${weather.api.page-no}")
    private int pageNo;
    
    @Value("${weather.api.data-type}")
    private String dataType;
    
    /**
     * 특정 날짜, 시간, 좌표에 대한 날씨 정보를 조회합니다.
     * 
     * @param date 날짜 (YYYYMMDD)
     * @param time 시간 (HHMM)
     * @param nx X 좌표
     * @param ny Y 좌표
     * @return 날씨 정보
     */
    public WeatherInfoDto getWeatherInfo(String date, String time, Integer nx, Integer ny) {
        try {
            log.info("날씨 정보 조회 요청: date={}, time={}, nx={}, ny={}", date, time, nx, ny);
            
            // 기상청 API 호출
            WeatherResponseDto response = callWeatherApi(date, time, nx, ny);
            
            // 응답 데이터 파싱
            WeatherInfoDto weatherInfo = parseWeatherResponse(response, date, time);
            
            log.info("날씨 정보 조회 완료: {}", weatherInfo);
            return weatherInfo;
            
        } catch (Exception e) {
            log.error("날씨 정보 조회 중 오류 발생", e);
            // 오류 발생 시 기본값 반환
            throw e;
        }
    }
    
    /**
     * 기상청 API를 호출합니다.
     */
    private WeatherResponseDto callWeatherApi(String date, String time, Integer nx, Integer ny) {
        String url = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("dataType", dataType)
                .queryParam("base_date", date)
                .queryParam("base_time", time)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .build()
                .toUriString();
        
        log.debug("기상청 API 호출 URL: {}", url);
        
        WebClient webClient = webClientBuilder.build();
        
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(WeatherResponseDto.class)
                .timeout(Duration.ofSeconds(2))
                .block();
    }
    
    /**
     * 기상청 API 응답을 파싱하여 WeatherInfoDto로 변환합니다.
     */
    private WeatherInfoDto parseWeatherResponse(WeatherResponseDto response, String date, String time) {
        if (response == null || response.getResponse() == null || 
            response.getResponse().getBody() == null || 
            response.getResponse().getBody().getItems() == null) {
            throw new RuntimeException("기상청 API 응답이 올바르지 않습니다.");
        }
        
        List<WeatherResponseDto.WeatherItem> items = response.getResponse().getBody().getItems().getItem();
        
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("날씨 데이터가 없습니다.");
        }
        
        // 요청한 날짜와 시간에 맞는 데이터만 필터링
        List<WeatherResponseDto.WeatherItem> filteredItems = items.stream()
                .filter(item -> date.equals(item.getFcstDate()) && time.equals(item.getFcstTime()))
                .collect(Collectors.toList());
        
        // 카테고리별로 데이터 매핑
        Map<String, String> weatherData = filteredItems.stream()
                .collect(Collectors.toMap(
                        WeatherResponseDto.WeatherItem::getCategory,
                        WeatherResponseDto.WeatherItem::getFcstValue,
                        (existing, replacement) -> existing
                ));
        
        // WeatherInfoDto 생성
        return WeatherInfoDto.builder()
                .temperature(parseTemperature(weatherData.get("T1H")))
                .precipitation(weatherData.get("RN1"))
                .skyCondition(weatherData.get("SKY"))
                .precipitationType(weatherData.get("PTY"))
                .forecastDate(date)
                .forecastTime(time)
                .build();
    }
    
    /**
     * 기온 문자열을 Double로 변환합니다.
     */
    private Double parseTemperature(String temperatureStr) {
        if (temperatureStr == null || temperatureStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return Double.parseDouble(temperatureStr);
        } catch (NumberFormatException e) {
            log.warn("기온 파싱 실패: {}", temperatureStr);
            return null;
        }
    }
    
    /**
     * 현재 시간 기준으로 적절한 발표 시간을 계산합니다.
     * 기상청 API는 매시각 45분 이후에 호출해야 합니다.
     */
    public String calculateBaseTime() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        
        // 45분 이후라면 현재 시간, 그렇지 않다면 이전 시간
        if (minute >= 45) {
            return String.format("%02d%02d", hour, 30);
        } else {
            hour = (hour == 0) ? 23 : hour - 1;
            return String.format("%02d%02d", hour, 30);
        }
    }
    
    /**
     * 현재 날짜를 YYYYMMDD 형식으로 반환합니다.
     */
    public String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
