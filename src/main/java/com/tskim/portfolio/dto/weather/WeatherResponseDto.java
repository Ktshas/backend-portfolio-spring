package com.tskim.portfolio.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WeatherResponseDto {
    
    @JsonProperty("response")
    private Response response;
    
    @Data
    public static class Response {
        @JsonProperty("header")
        private Header header;
        
        @JsonProperty("body")
        private Body body;
    }
    
    @Data
    public static class Header {
        @JsonProperty("resultCode")
        private String resultCode;
        
        @JsonProperty("resultMsg")
        private String resultMsg;
    }
    
    @Data
    public static class Body {
        @JsonProperty("dataType")
        private String dataType;
        
        @JsonProperty("items")
        private Items items;
        
        @JsonProperty("pageNo")
        private Integer pageNo;
        
        @JsonProperty("numOfRows")
        private Integer numOfRows;
        
        @JsonProperty("totalCount")
        private Integer totalCount;
    }
    
    @Data
    public static class Items {
        @JsonProperty("item")
        private List<WeatherItem> item;
    }
    
    @Data
    public static class WeatherItem {
        @JsonProperty("baseDate")
        private String baseDate;
        
        @JsonProperty("baseTime")
        private String baseTime;
        
        @JsonProperty("category")
        private String category;
        
        @JsonProperty("fcstDate")
        private String fcstDate;
        
        @JsonProperty("fcstTime")
        private String fcstTime;
        
        @JsonProperty("fcstValue")
        private String fcstValue;
        
        @JsonProperty("nx")
        private Integer nx;
        
        @JsonProperty("ny")
        private Integer ny;
    }
}
