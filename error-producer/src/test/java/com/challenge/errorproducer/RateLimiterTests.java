package com.challenge.errorproducer;

import com.challenge.errorproducer.config.RateLimitConfig;
import com.challenge.errorproducer.utils.RateLimiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RateLimiterTests {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RateLimiter redisRateLimiter;

    @Test
    void whenLimitNotReached_allowRequest() {
        // Setup
        String userId = "user1";
        String actionType = "news";
        RateLimitConfig config = new RateLimitConfig();
        config.setTypes(List.of(new RateLimitConfig.Type(actionType, 1, 60000)));

        redisRateLimiter = new RateLimiter(redisTemplate, config);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("rate:news:user1")).thenReturn(1L);
        when(redisTemplate.expire(anyString(), anyLong(), any())).thenReturn(true);

        // Act
        boolean allowed = redisRateLimiter.isAllowed(userId, actionType);

        // Assert
        assertThat(allowed).isTrue();
    }

    @Test
    void whenLimitReached_rejectRequest() {
        // Setup
        String userId = "user1";
        String actionType = "news";
        RateLimitConfig config = new RateLimitConfig();
        config.setTypes(List.of(new RateLimitConfig.Type(actionType, 1, 60000)));

        redisRateLimiter = new RateLimiter(redisTemplate, config);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("rate:news:user1")).thenReturn(2L);  // Limit is 1

        // Act
        boolean allowed = redisRateLimiter.isAllowed(userId, actionType);

        // Assert
        assertThat(allowed).isFalse();
    }
}
