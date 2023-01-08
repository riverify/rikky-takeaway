package com.fubukiss.rikky.common;

/**
 * <p>Project: rikky-takeaway - CustomException 自定义异常类
 * <p>Powered by river On 2023/01/08 8:52 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
public class CustomException extends RuntimeException {
    /**
     * 这个异常将会被{@link GlobalExceptionHandler}捕获，然后返回给前端。
     *
     * @param message 异常信息
     */
    public CustomException(String message) {
        super(message);
    }

}
