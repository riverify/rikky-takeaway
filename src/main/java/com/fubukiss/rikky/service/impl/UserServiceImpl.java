package com.fubukiss.rikky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fubukiss.rikky.common.CustomException;
import com.fubukiss.rikky.entity.User;
import com.fubukiss.rikky.mapper.UserMapper;
import com.fubukiss.rikky.service.UserService;
import com.fubukiss.rikky.util.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
    private int TIME_OUT_MINUTES;

    /**
     * RedisTemplate redis操作模板
     */
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


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
        simpleMailMessage.setText("欢迎使用悦刻外卖平台\n您的验证码为：" + code + "，请在" + TIME_OUT_MINUTES + "分钟内使用！\n【该邮件为系统自动发送，请勿回复】");
        // 将验证码存入redis，设置失效时间TIME_OUT_MINUTES分钟
        redisTemplate.opsForValue().set(email, code, TIME_OUT_MINUTES, java.util.concurrent.TimeUnit.MINUTES);
        // 发送邮件
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            e.printStackTrace();
            throw new CustomException("致命错误！");
        }


    }


    /**
     * 验证码登陆账号，如果是新用户，则自动注册
     *
     * @param email   邮箱地址
     * @param code    验证码
     * @param session 会话
     */
    @Override
    public User loginByVerificationCode(String email, String code, HttpSession session) {
        // 获取redis中的验证码
        Object verificationCodeInRedis = redisTemplate.opsForValue().get(email);
        // 验证码是否正确
        if (verificationCodeInRedis != null && verificationCodeInRedis.equals(code)) {
            // 验证码正确
            // 查询用户是否存在
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail, email);
            User user = this.getOne(queryWrapper);// 查询用户
            // 用户不存在，自动注册
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setStatus(1);      // 设置用户状态为正常
                this.save(user);
            }
            // 将用户信息存入session
            session.setAttribute("user", user.getId());
            // 将redis中的验证码删除
            redisTemplate.delete(email);

            return user;
        } else {
            // 验证码错误
            throw new CustomException("验证码错误！");
        }
    }
}
