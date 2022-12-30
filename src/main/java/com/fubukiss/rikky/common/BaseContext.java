package com.fubukiss.rikky.common;

/**
 * <p>Project: rikky-takeaway - BaseContext 基于ThreadLocal封装的工具类，用于获取当前线程的用户信息
 * <p>Powered by Riverify On 12-28-2022 22:00:30
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static long getCurrentId() {
        return threadLocal.get();
    }
}
