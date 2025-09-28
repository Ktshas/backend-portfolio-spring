package com.tskim.portfolio.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "portfolio_projects")
@Data
@EqualsAndHashCode(callSuper = true)
public class PortfolioProject extends BaseDocument {
    
    private String title;
    private String description;
    private String category;
    private String[] technologies;
    private String githubUrl;
    private String liveUrl;
    private String[] imageUrls;
    private Map<String, Object> metadata; // 유연한 메타데이터 저장
    private Boolean isPublished = false;
}
