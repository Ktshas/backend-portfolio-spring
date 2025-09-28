package com.tskim.portfolio.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "running_schedules")
@Data
@EqualsAndHashCode(callSuper = true)
public class RunningSchedule extends BaseDocument {
    
    private String title;              // 제목
    private String date;               // 날짜 (YYYYMMDD 형식)
    private String yearMonth;          // 년월 (YYYYMM 형식) - 검색 최적화용
    private String startTime;          // 시작시간 (HHMM 형식)
    private String endTime;            // 종료시간 (HHMM 형식)
    private Integer x;                 // X 좌표 (기상청 API nx)
    private Integer y;                 // Y 좌표 (기상청 API ny)
    private String placeName;           // 주소
    private String placeDetail;     // 상세장소
    private String placeUrl;
    private String addressName;
}
