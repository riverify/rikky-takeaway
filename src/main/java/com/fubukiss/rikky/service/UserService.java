package com.fubukiss.rikky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fubukiss.rikky.entity.User;

import javax.servlet.http.HttpSession;

/**
 * FileName: UserService
 * Date: 2023/01/16
 * Time: 20:24
 * Author: river
 */
public interface UserService extends IService<User> {

    /**
     * 发送验证码
     *
     * @param email   邮箱地址
     * @param session 会话
     */
    void sendCode(String email, HttpSession session);


    /**
     * 验证码登陆账号，如果是新用户，则自动注册
     *
     * @param email   邮箱地址
     * @param code    验证码
     * @param session 会话
     * @return 用户信息
     */
    User loginByVerificationCode(String email, String code, HttpSession session);
}
