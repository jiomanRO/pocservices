package com.orange.john.frontservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
@EnableRedisHttpSession
public class RedisConfiguration extends AbstractHttpSessionApplicationInitializer {
    @Value("${redis.server.ip}")
    private String redisServerIp;

    @Value("${redis.server.port}")
    private int redisServerPort;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisServerIp, redisServerPort);

//        redisStandaloneConfiguration.setPassword(RedisPassword.of("yourRedisPasswordIfAny"));
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //Maximum number of active connections that can be allocated from this pool at the same time
        poolConfig.setMaxTotal(256);
        //Number of connections to Redis that just sit there and do nothing
        poolConfig.setMaxIdle(12);
        //Minimum number of idle connections to Redis - these can be seen as always open and ready to serve
        poolConfig.setMinIdle(6);
        //The maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception
        poolConfig.setMaxWaitMillis(1000);
        //The minimum amount of time an object may sit idle in the pool before it is eligible for eviction by the idle object evictor
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        //The minimum amount of time a connection may sit idle in the pool before it is eligible for eviction by the idle connection evictor
        poolConfig.setSoftMinEvictableIdleTimeMillis(Duration.ofSeconds(10).toMillis());
        //Idle connection checking period
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(5).toMillis());
        //Maximum number of connections to test in each idle check
        poolConfig.setNumTestsPerEvictionRun(3);
        //Tests whether connection is dead when connection retrieval method is called
        poolConfig.setTestOnBorrow(true);
        //Tests whether connection is dead when returning a connection to the pool
        poolConfig.setTestOnReturn(true);
        //Tests whether connections are dead during idle periods
        poolConfig.setTestWhileIdle(true);
        poolConfig.setBlockWhenExhausted(true);
        //JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);

        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().poolConfig(poolConfig).build();
        //return new JedisConnectionFactory(redisStandaloneConfiguration);
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
