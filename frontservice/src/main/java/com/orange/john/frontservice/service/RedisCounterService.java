package com.orange.john.frontservice.service;

import com.orange.john.frontservice.model.RedisCounter;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RedisCounterService {
    Optional<RedisCounter> findById(String sessionId);
    void save(RedisCounter redisCounter);
    void incrementCounter(String sessionId);
    void setCounter(String sessionId, int counter);
    void deleteById(String sessionId);
    void setLastAccessedTime(String sessionId, LocalDateTime localDateTime);
}
