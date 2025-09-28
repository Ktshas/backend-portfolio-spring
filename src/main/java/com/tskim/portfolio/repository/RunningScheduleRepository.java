package com.tskim.portfolio.repository;

import com.tskim.portfolio.entity.RunningSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunningScheduleRepository extends MongoRepository<RunningSchedule, String> {
    
    // 날짜별 스케줄 조회 (기상청 API 형식: YYYYMMDD)
    List<RunningSchedule> findByDate(String date);
    
    // 날짜 범위별 스케줄 조회 (기상청 API 형식: YYYYMMDD)
    List<RunningSchedule> findByDateBetween(String startDate, String endDate);
    
    // 년월별 스케줄 조회 (YYYYMM 형식) - 검색 최적화
    List<RunningSchedule> findByYearMonthOrderByDateAsc(String yearMonth);
}
