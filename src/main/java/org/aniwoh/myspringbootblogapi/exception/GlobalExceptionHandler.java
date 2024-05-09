package org.aniwoh.myspringbootblogapi.exception;

import org.aniwoh.myspringbootblogapi.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.entity.ResultCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error(String.valueOf(e));
        return Result.error(ResultCode.ERROR,StringUtils.hasLength(e.getMessage())? e.getMessage():"操作失败");

    }
}
