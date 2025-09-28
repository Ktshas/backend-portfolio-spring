package com.tskim.portfolio.dto;

import com.tskim.portfolio.entity.PortfolioProject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class PortfolioProjectDto {
    private String id;
    private String title;
    private String description;
    private String category;
    private String[] technologies;
    private String githubUrl;
    private String liveUrl;
    private String[] imageUrls;
    private Map<String, Object> metadata;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static PortfolioProjectDto from(PortfolioProject project) {
        PortfolioProjectDto dto = new PortfolioProjectDto();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setCategory(project.getCategory());
        dto.setTechnologies(project.getTechnologies());
        dto.setGithubUrl(project.getGithubUrl());
        dto.setLiveUrl(project.getLiveUrl());
        dto.setImageUrls(project.getImageUrls());
        dto.setMetadata(project.getMetadata());
        dto.setIsPublished(project.getIsPublished());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }
}
