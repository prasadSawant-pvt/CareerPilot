package com.pathprep.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthCheckController implements HealthIndicator {

    private final MongoClient mongoClient;

    @Override
    public Health health() {
        try {
            // Test MongoDB connection
            MongoDatabase db = mongoClient.getDatabase("admin");
            Document pingCommand = new Document("ping", 1);
            db.runCommand(pingCommand);
            
            return Health.up()
                .withDetail("database", "MongoDB")
                .withDetail("status", "UP")
                .withDetail("timestamp", LocalDateTime.now())
                .build();
        } catch (Exception e) {
            log.error("MongoDB health check failed: {}", e.getMessage());
            return Health.down()
                .withDetail("database", "MongoDB")
                .withDetail("status", "DOWN")
                .withDetail("error", e.getMessage())
                .withDetail("timestamp", LocalDateTime.now())
                .build();
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> healthCheck() {
        Health health = health();
        // Health.Builder does not have getStatus(); use Health.getStatus() instead
        if (health.getStatus().equals(Status.UP)) {
            return ResponseEntity.ok(health.getDetails());
        } else {
            return ResponseEntity.status(503).body(health.getDetails());
        }
    }
}
