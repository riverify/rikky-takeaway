package com.fubukiss.rikky.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * <p>Project: rikky-takeaway - GlobalExceptionHandler 全局异常处理
 * <p>Powered by Riverify On 12-17-2022 23:21:42
 *
 * <p>通过 @ControllerAdvice注解声明一个控制器建言，对有 @RestController和 @Controller注解的控制器的方法加一些公共的操作
 * <p>通过 @ResponseBody注解将返回的数据转换成 JSON 格式
 * <p>通过 @Slf4j注解声明一个日志对象
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理SQLIntegrityConstraintViolationException异常
     *
     * @param exception 异常
     * @return 返回一个通用的结果对象
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error("SQLIntegrityConstraintViolationException异常: {}", exception.getMessage());
        return R.error("操作失败");
    }
}
