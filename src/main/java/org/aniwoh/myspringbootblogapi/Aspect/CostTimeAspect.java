package org.aniwoh.myspringbootblogapi.Aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class CostTimeAspect {

    @Around("@annotation(CostTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        // 执行目标方法
        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        String methodName = joinPoint.getSignature().toShortString();
        log.info("方法 {} 执行耗时: {} ms", methodName, duration);
        return result;
    }
}
