package com.fubukiss.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;

/**
 * FileName: MailSendTest
 * Date: 2023/01/16
 * Time: 23:41
 * Author: river
 */
@Controller
public class MailSendTest {
    @Autowired
    JavaMailSender javaMailSender;

    @Test
    public void sendMail() {
        javaMailSender = new JavaMailSenderImpl();
        // 生成4位验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        // 发送邮件
        // 构建一个邮件对象
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        // 设置邮件发送者
        simpleMailMessage.setFrom("no-reply-me@foxmail.com");
        simpleMailMessage.setSubject("Test");
        simpleMailMessage.setTo("1105799454@qq.com");
        simpleMailMessage.setText("test111");
        javaMailSender.send(simpleMailMessage);
    }
}
