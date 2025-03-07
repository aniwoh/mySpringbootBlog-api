package org.aniwoh.myspringbootblogapi.Aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int limit() default 2;   // 允许的最大请求数
    int seconds() default 10; // 限流窗口时间（秒）
}
