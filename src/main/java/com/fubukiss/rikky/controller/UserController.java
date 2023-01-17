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

}
