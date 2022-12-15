package com.fubukiss.rikky.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体类
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    /**
     * 身份证号码
     * <p>使用了驼峰命名法，和数据库 employee 表中的 id_number 不同。
     * <p>通过在 application.yml 的 mybatis-plus.configuration.map-underscore-to-camel-case 属性设置为 true
     * Mybatis Plus 帮助我们自动实现驼峰命名向 underline 模式的转换。
     * <p>之后的几个变量亦是如此。
     */
    private String idNumber;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
