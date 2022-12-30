package com.fubukiss.rikky.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>Project: rikky-takeaway - MetaObjectHandler 数据库相应字段的自动填充，自定义元数据对象处理器
 * <p>Powered by Riverify On 12-27-2022 20:05:51
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充[insert]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());     // 从 ThreadLocal 中获取当前用户的 id
        metaObject.setValue("updateUser", BaseContext.getCurrentId());     // 从 ThreadLocal 中获取当前用户的 id
    }


    /**
     * 更新时自动填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充[update]...");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();                            // 获取当前线程的id
        log.info("当前线程id={}", id);                                        // Slf4j的日志输出

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());  // 从ThreadLocal中获取当前线程的用户id
    }
}
