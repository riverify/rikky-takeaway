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

    /**
     * 员工id
     */
    private Long id;

    /** 用户名 */
    private String username;

    /** 姓名 */
    private String name;

    /** 密码 */
    private String password;

    /** 手机号码 */
    private String phone;

    /** 性别 */
    private String sex;

    /**
     * 身份证号码
     * <p>使用了驼峰命名法，和数据库 employee 表中的 id_number 不同。
     * <p>通过在 application.yml 的 mybatis-plus.configuration.map-underscore-to-camel-case 属性设置为 true
     * Mybatis Plus 帮助我们自动实现驼峰命名向 underline 模式的转换。
     * <p>之后的几个变量亦是如此。
     */
    private String idNumber;

    /** 员工状态：1为在职 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT) // 自动填充，插入时自动填充 Mybatis Plus功能
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE) // 自动填充，插入和更新时自动填充
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)    // 自动填充，插入时自动填充
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 自动填充，插入和更新时自动填充
    private Long updateUser;

}
