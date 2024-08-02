package com.dev.cloudnative.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class HelloController {

//    @RateLimiter(name = "hello")
//    @GetMapping("/hello")
//    public String hello() {
//        return "{\"msg\":\"hello\"}";
//    }
    private static final Logger logger = Logger.getLogger(HelloController.class.getName());

    /* distribution*/
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisScript<Long> rateLimiterScript;

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        String key = "rate:limit:hello";
        Long limit = redisTemplate.execute(rateLimiterScript, Collections.singletonList(key), "100");
        if (limit != null && limit == 1) {
            logger.info("Request accepted");
            return ResponseEntity.ok("{\"msg\":\"hello\"}");
        } else {
            logger.info("Too many requests");
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("{\"msg\":\"Too many requests\"}");
        }
    }
}
