package org.aniwoh.myspringbootblogapi.interceptors;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.aniwoh.myspringbootblogapi.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        try {
            JWT jwt = JWTUtil.parseToken(token);
            Map<String,Object> map = jwt.getPayloads();

            log.info(map.toString());


            ThreadLocalUtil.set(map);
            return true;
        } catch (Exception e){
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 释放ThreadLocal, 防止内存泄漏
        ThreadLocalUtil.remove();
    }
}
