package com.dev.cloudnative.config;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisScriptingCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

@Configuration
public class RedisRateLimiterConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisScript<Long> rateLimiterScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(rateLimiterLuaScript());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    private String rateLimiterLuaScript() {
        return "local key = KEYS[1]\n" +
                "local limit = tonumber(ARGV[1])\n" +
                "local current = tonumber(redis.call('get', key) or '0')\n" +
                "if current + 1 > limit then\n" +
                "  return 0\n" +
                "else\n" +
                "  redis.call('incrby', key, 1)\n" +
                "  redis.call('expire', key, 1)\n" +
                "  return 1\n" +
                "end";
    }
}
