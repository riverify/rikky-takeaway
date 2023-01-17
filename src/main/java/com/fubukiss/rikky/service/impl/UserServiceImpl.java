package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.common.CustomException;
import com.fubukiss.rikky.entity.User;
import com.fubukiss.rikky.mapper.UserMapper;
import com.fubukiss.rikky.service.UserService;
import com.fubukiss.rikky.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * FileName: UserServiceImpl 用户服务实现类
 * Date: 2023/01/16
 * Time: 20:25
 * Author: river
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService { // 通过继承ServiceImpl，实现了基本的增删改查方法

    /**
     * 邮箱发送器
     */
    @Autowired
    private JavaMailSender javaMailSender;


    /**
     * 发送验证码的邮箱地址
     */
    @Value("${spring.mail.username}")
    private String FROM;

    /**
     * 失效时间
     */
    @Value("${spring.mail.timeout}")
    private int TIME_OUT;


    /**
     * 发送验证码
     *
     * @param email   邮箱地址
     * @param session 会话
     */
    public void sendCode(String email, HttpSession session) {
        // 生成4位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("Session:{}, Code:{}, to {}", session, code, email);      // Slf4j的日志输出
        // 设置session过期时间
        session.setMaxInactiveInterval(60 * TIME_OUT);
        // 发送邮件
        // 构建一个邮件对象
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置邮件发送者
        simpleMailMessage.setFrom(FROM);
        // 设置邮件接收者
        simpleMailMessage.setTo(email);
        // 设置邮件主题
        simpleMailMessage.setSubject("[悦刻外卖]登陆验证码");
        // 设置邮件内容
        simpleMailMessage.setText("欢迎使用悦刻外卖平台\n您的验证码为：" + code + "，请在" + TIME_OUT + "分钟内使用！\n【该邮件为系统自动发送，请勿回复】");
        // 将验证码存入session
        session.setAttribute("verificationCode", code);
        // 发送邮件
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            throw new CustomException("致命错误！");
        }
    }
}
