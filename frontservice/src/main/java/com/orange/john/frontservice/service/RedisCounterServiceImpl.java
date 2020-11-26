package com.orange.john.frontservice.service;

import com.orange.john.frontservice.model.RedisCounter;
import com.orange.john.frontservice.repository.RedisCounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RedisCounterServiceImpl implements RedisCounterService {

    private final RedisCounterRepository redisCounterRepository;

    public RedisCounterServiceImpl(RedisCounterRepository redisCounterRepository) {
        this.redisCounterRepository = redisCounterRepository;
    }

    @Override
    public Optional<RedisCounter> findById(String sessionId) {
        return redisCounterRepository.findById(sessionId);
    }

    @Override
    public void save(RedisCounter redisCounter) {
        redisCounterRepository.save(redisCounter);
    }

    @Override
    public void incrementCounter(String sessionId) {
        Optional<RedisCounter> redisCounter = redisCounterRepository.findById(sessionId);
        if(redisCounter.isPresent()) {
            Integer counter = redisCounter.get().getCounter();
            counter++;
            redisCounter.get().setCounter(counter);
            redisCounterRepository.save(redisCounter.get());
        }
    }

    @Override
    public void setCounter(String sessionId, int counter) {
        Optional<RedisCounter> redisCounter = redisCounterRepository.findById(sessionId);
        if(redisCounter.isPresent()) {
            redisCounter.get().setCounter(counter);
            redisCounterRepository.save(redisCounter.get());
        }
    }

    @Override
    public void deleteById(String sessionId) {
        redisCounterRepository.deleteById(sessionId);
    }

    @Override
    public void setLastAccessedTime(String sessionId, LocalDateTime localDateTime) {
        Optional<RedisCounter> redisCounter = redisCounterRepository.findById(sessionId);
        if(redisCounter.isPresent()) {
            redisCounter.get().setLastAccessedTime(localDateTime);
            redisCounterRepository.save(redisCounter.get());
        }
    }
}
