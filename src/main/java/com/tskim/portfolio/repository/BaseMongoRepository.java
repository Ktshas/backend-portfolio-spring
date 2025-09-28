package com.tskim.portfolio.repository;

import com.tskim.portfolio.entity.BaseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseMongoRepository extends MongoRepository<BaseDocument, String> {
    // MongoDB Repositories will be added here
}
