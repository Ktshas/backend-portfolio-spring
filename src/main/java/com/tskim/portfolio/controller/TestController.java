package com.tskim.portfolio.controller;

import com.tskim.portfolio.repository.RunningScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private RunningScheduleRepository runningScheduleRepository;

    @GetMapping("/mongodb")
    public ResponseEntity<Map<String, Object>> testMongoDB() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // MongoDB 연결 테스트
            String databaseName = mongoTemplate.getDb().getName();
            response.put("status", "success");
            response.put("message", "MongoDB 연결 성공");
            response.put("database", databaseName);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "MongoDB 연결 실패: " + e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @GetMapping("/repository")
    public ResponseEntity<Map<String, Object>> testRepository() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Repository를 통한 연결 테스트
            long count = runningScheduleRepository.count();
            response.put("status", "success");
            response.put("message", "MongoRepository 연결 성공");
            response.put("scheduleCount", count);
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "MongoRepository 연결 실패: " + e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(response);
        }
    }
}
