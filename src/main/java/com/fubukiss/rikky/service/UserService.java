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
}
