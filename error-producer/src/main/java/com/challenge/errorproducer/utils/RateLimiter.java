package com.challenge.errorproducer.utils;

import com.challenge.errorproducer.config.RateLimitConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimiter {
    private final StringRedisTemplate redisTemplate;
    private final RateLimitConfig rateLimitConfig;

    @Autowired
    public RateLimiter(StringRedisTemplate redisTemplate, RateLimitConfig rateLimitConfig) {
        this.redisTemplate = redisTemplate;
        this.rateLimitConfig = rateLimitConfig;
    }

    public boolean isAllowed(String userId, String actionType) {
        RateLimitConfig.Type typeConfig = rateLimitConfig.getTypes().stream()
                .filter(t -> t.getType().equalsIgnoreCase(actionType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported message type: " + actionType));

        String key = "rate:" + actionType + ":" + userId;
        long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, typeConfig.getTimeLimit(), TimeUnit.MILLISECONDS);
        }
        return count <= typeConfig.getMaxCount();
    }
}
