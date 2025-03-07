package org.aniwoh.myspringbootblogapi.Aspect;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.exception.RateLimitException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class RateLimitAspect {

    @Resource
    private RedisTemplate<String, Integer> redisTemplate;


    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String key = "rate_limit:" + methodName;

        // 读取 Redis 中的请求次数
        Integer count = redisTemplate.opsForValue().get(key);

        if (count == null) {
            // 第一次请求，初始化计数
            redisTemplate.opsForValue().set(key, 1, rateLimit.seconds(), TimeUnit.SECONDS);
        } else if (count < rateLimit.limit()) {
            // 未超过限流次数，自增
            log.info("准备自增");
            redisTemplate.opsForValue().increment(key);
        } else {
            log.warn("接口 {} 触发限流，限流窗口 {} 秒内最多 {} 次", methodName, rateLimit.seconds(), rateLimit.limit());
            throw new RateLimitException("请求过于频繁，请稍后重试！");
        }

        return joinPoint.proceed();
    }


}
