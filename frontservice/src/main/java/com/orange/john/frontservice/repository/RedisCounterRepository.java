package com.orange.john.frontservice.repository;

import com.orange.john.frontservice.model.RedisCounter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisCounterRepository extends CrudRepository<RedisCounter, String> {
}
