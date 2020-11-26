package com.orange.john.frontservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash(value = "RedisCounter", timeToLive = 3600)
public class RedisCounter implements Serializable {
    @Id
    private String sessionId;
    private Integer counter;
    private LocalDateTime lastAccessedTime;

    public RedisCounter(String sessionId, int counter, LocalDateTime lastAccessedTime) {
        this.sessionId = sessionId;
        this.counter = counter;
        this.lastAccessedTime = lastAccessedTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public LocalDateTime getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(LocalDateTime lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public void incrementCounter() {
        this.counter++;
    }
}
