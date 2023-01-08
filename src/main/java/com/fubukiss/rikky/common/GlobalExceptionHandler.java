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
     * 处理异常{@link SQLIntegrityConstraintViolationException}，本项目中主要处理注册时输入重复数据的异常
     * <p>SQL报错的错误信息进行切片处理，返回特定的错误信息，即某个账号已存在。
     * <p>通过 @ExceptionHandler注解声明该方法是一个异常处理方法，可以处理的异常类型为 {@link SQLIntegrityConstraintViolationException}。
     *
     * @param exception 异常，example：com.mysql.cj.jdbc.exceptions.SQLIntegrityConstraintViolationException: Duplicate entry 'riverify' for key 'username'
     * @return 返回一个通用的结果对象
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error("SQLIntegrityConstraintViolationException异常: {}", exception.getMessage());
        // 当异常信息中包含“Duplicate entry”时，说明是主键冲突异常，返回数据重复的错误信息
        if (exception.getMessage().contains("Duplicate entry")) {
            // 提取出错误的具体情况，并返回给前端
            String[] split = exception.getMessage().split("'"); // Duplicate entry 'riverify' for key 'employee.idx_username' 以单引号切片
            return R.error(split[1] + " 已存在"); // 返回数据重复的账号, 如 riverify已存在
        }
        // 其他异常，返回未知错误
        return R.error("操作失败，未知错误");
    }

    /**
     * 处理自定义异常{@link CustomException}，通过在程序中使用如：throw new CustomException("xxx")抛出异常后，在这里被捕获并返回给前端(使用R对象包装)
     * <p>通过 @ExceptionHandler注解声明该方法是一个异常处理方法，可以处理的异常类型为 {@link CustomException}。
     *
     * @param exception 自定义异常
     * @return 返回一个通用的结果对象，用于前端错误内容的展示
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception) {
        log.error("发生自定义异常: {}", exception.getMessage());
        // 其他异常，返回未知错误
        return R.error(exception.getMessage());
    }
}
