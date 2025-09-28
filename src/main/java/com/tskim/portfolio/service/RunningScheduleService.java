package com.tskim.portfolio.service;

import com.tskim.portfolio.dto.RunningScheduleDto;
import com.tskim.portfolio.dto.WeatherInfoDto;
import com.tskim.portfolio.entity.RunningSchedule;
import com.tskim.portfolio.repository.RunningScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RunningScheduleService {
    
    private final RunningScheduleRepository runningScheduleRepository;
    private final WeatherService weatherService;
    
    /**
     * 러닝 스케줄 등록
     */
    @Transactional
    public RunningScheduleDto createSchedule(RunningScheduleDto scheduleDto) {
        log.info("러닝 스케줄 등록 요청: {}", scheduleDto.getTitle());
        
        // 시간 검증
        validateScheduleTime(scheduleDto);
        
        RunningSchedule schedule = scheduleDto.toEntity();
        RunningSchedule savedSchedule = runningScheduleRepository.save(schedule);
        
        log.info("러닝 스케줄 등록 완료: ID={}, 제목={}", savedSchedule.getId(), savedSchedule.getTitle());
        
        return RunningScheduleDto.from(savedSchedule);
    }
    
    /**
     * 특정 년월의 러닝 스케줄 조회
     */
    public List<RunningScheduleDto> getSchedulesByMonth(String yearMonth) {
        log.info("특정 년월 러닝 스케줄 조회 요청: {}", yearMonth);
        
        List<RunningSchedule> schedules = runningScheduleRepository.findByYearMonthOrderByDateAsc(yearMonth);
        
        return schedules.stream()
                .map(RunningScheduleDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 날짜의 러닝 스케줄 조회 (기상청 API 형식: YYYYMMDD)
     */
    public List<RunningScheduleDto> getSchedulesByDate(String date) {
        log.info("날짜별 러닝 스케줄 조회 요청: {}", date);
        
        List<RunningSchedule> schedules = runningScheduleRepository.findByDate(date);
        
        return schedules.stream()
                .map(RunningScheduleDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * 날짜 범위별 러닝 스케줄 조회 (기상청 API 형식: YYYYMMDD)
     */
    public List<RunningScheduleDto> getSchedulesByDateRange(String startDate, String endDate) {
        log.info("날짜 범위별 러닝 스케줄 조회 요청: {} ~ {}", startDate, endDate);
        
        List<RunningSchedule> schedules = runningScheduleRepository.findByDateBetween(startDate, endDate);
        
        return schedules.stream()
                .map(RunningScheduleDto::from)
                .collect(Collectors.toList());
    }
    
    /**
     * ID로 러닝 스케줄 조회 (날씨 정보 포함)
     */
    public RunningScheduleDto getScheduleById(String id) {
        log.info("ID로 러닝 스케줄 조회 요청: {}", id);
        
        RunningSchedule schedule = runningScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 러닝 스케줄을 찾을 수 없습니다: " + id));
        
        RunningScheduleDto scheduleDto = RunningScheduleDto.from(schedule);
        
        // 날씨 정보 조회
        try {
            WeatherInfoDto weatherInfo = weatherService.getWeatherInfo(
                    schedule.getDate(),
                    schedule.getStartTime(),
                    schedule.getX(),
                    schedule.getY()
            );
            scheduleDto.setWeatherInfo(weatherInfo);
            log.info("날씨 정보 조회 완료: 스케줄 ID={}", id);
        } catch (Exception e) {
            log.warn("날씨 정보 조회 실패: 스케줄 ID={}, 오류={}", id, e.getMessage());
            // 날씨 정보 조회 실패 시 null로 설정
            scheduleDto.setWeatherInfo(null);
        }
        
        return scheduleDto;
    }
    
    
    /**
     * 스케줄 시간 검증 (기상청 API 형식: HHMM)
     */
    private void validateScheduleTime(RunningScheduleDto scheduleDto) {
        if (scheduleDto.getStartTime() != null && scheduleDto.getEndTime() != null) {
            // HHMM 형식의 문자열을 직접 비교
            String startTime = scheduleDto.getStartTime();
            String endTime = scheduleDto.getEndTime();
            
            if (endTime.compareTo(startTime) <= 0) {
                throw new IllegalArgumentException("종료시간은 시작시간보다 늦어야 합니다");
            }
        }
    }
}
