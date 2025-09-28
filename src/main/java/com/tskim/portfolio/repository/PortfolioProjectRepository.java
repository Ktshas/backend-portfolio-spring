package com.tskim.portfolio.repository;

import com.tskim.portfolio.entity.PortfolioProject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioProjectRepository extends MongoRepository<PortfolioProject, String> {
    List<PortfolioProject> findByCategory(String category);
    List<PortfolioProject> findByIsPublished(Boolean isPublished);
    List<PortfolioProject> findByTechnologiesContaining(String technology);
}
