package org.aniwoh.myspringbootblogapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.entity.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.dev33.satoken.exception.DisableServiceException;
import cn.dev33.satoken.exception.NotHttpBasicAuthException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.NotSafeException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 拦截：未登录异常
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Result> handlerException(NotLoginException e) {
        log.error(e.toString());
        // 返回给前端
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error(ResultCode.ERROR,"请先登录"));
    }

    // 拦截：缺少权限异常
    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<Result> handlerException(NotPermissionException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.error(ResultCode.ERROR,"该账号无访问权限"));

    }

    // 拦截：缺少角色异常
    @ExceptionHandler(NotRoleException.class)
    public ResponseEntity<Result> handlerException(NotRoleException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.error(ResultCode.ERROR,"缺少角色：" + e.getRole()));
    }

    // 拦截：二级认证校验失败异常
    @ExceptionHandler(NotSafeException.class)
    public ResponseEntity<Result> handlerException(NotSafeException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error(ResultCode.ERROR,"二级认证校验失败：" + e.getService()));
    }

    // 拦截：服务封禁异常
    @ExceptionHandler(DisableServiceException.class)
    public ResponseEntity<Result> handlerException(DisableServiceException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.error(ResultCode.ERROR,"当前账号 " + e.getService() + " 服务已被封禁 (level=" + e.getLevel() + ")：" + e.getDisableTime() + "秒后解封"));
    }

    // 拦截：Http Basic 校验失败异常
    @ExceptionHandler(NotHttpBasicAuthException.class)
    public ResponseEntity<Result> handlerException(NotHttpBasicAuthException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Result.error(ResultCode.ERROR,e.getMessage()));
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handlerException(Exception e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.error(ResultCode.ERROR,"未知异常"));
    }

}
