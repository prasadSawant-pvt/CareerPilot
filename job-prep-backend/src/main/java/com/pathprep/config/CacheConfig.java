package com.pathprep.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new ConcurrentMapCache("roadmaps"),
            new ConcurrentMapCache("roles"),
            new ConcurrentMapCache("skills"),
            new ConcurrentMapCache("interviewQuestions"),
            new ConcurrentMapCache("skillResources"),
            new ConcurrentMapCache("skillInterviewQuestions")
        ));
        return cacheManager;
    }
}
