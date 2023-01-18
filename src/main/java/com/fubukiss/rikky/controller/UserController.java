package com.fubukiss.rikky.controller;

import com.alibaba.druid.util.StringUtils;
import com.fubukiss.rikky.common.R;
import com.fubukiss.rikky.entity.User;
import com.fubukiss.rikky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * FileName: UserController 用户控制器
 * Date: 2023/01/16
 * Time: 20:27
 * Author: river
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 用户服务
     */
    @Autowired
    private UserService userService;

    /**
     * <h2>发送验证码<h2/>
     *
     * @param user    前端传来的用户实体类，主要是邮箱地址
     * @param session 会话
     * @return 通用返回类
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        log.info("发送验证码，用户信息：{}，session:{}", user, session);
        // 获得前端登陆邮箱地址
        String email = user.getEmail();
        // 判断邮箱地址是否为空
        if (!StringUtils.isEmpty(email)) {
            // 发送验证码
            userService.sendCode(email, session);    // 通过工具类发送验证码，sendCode方法在UserService接口中
            return R.success("发送成功，请及时查看邮箱");
        }
        return R.error("发送失败");
    }


    /**
     * <h2>验证码登陆账号<h2/>
     * <p>如果为新用户，则自动注册
     *
     * @param map     前端传来的用户实体类，主要是邮箱地址和验证码
     * @param session 会话
     * @return 返回User实体类，以在浏览器保存该信息
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info("用户登陆，用户信息：{}，session:{}", map.toString(), session);
        // 从map里获取邮箱地址和验证码
        String email = (String) map.get("email");
        String code = (String) map.get("code");
        // 判断邮箱地址和验证码是否为空
        if (!StringUtils.isEmpty(email) && !StringUtils.isEmpty(code)) {
            // 验证码登陆
            User user = userService.loginByVerificationCode(email, code, session);// 通过工具类发送验证码，login方法在UserService接口中
            return R.success(user);
        }
        return R.error("登陆失败，请检查邮箱地址和验证码");
    }

}
