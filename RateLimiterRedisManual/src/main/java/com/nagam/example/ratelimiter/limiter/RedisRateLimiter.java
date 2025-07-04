package com.nagam.example.ratelimiter.limiter;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;
    private static final int LIMIT = 50;
    private static final int WINDOW_SECONDS = 10;

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String key) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, WINDOW_SECONDS, TimeUnit.SECONDS);
        }
        return count <= LIMIT;
    }
}
