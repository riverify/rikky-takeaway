package com.fubukiss.rikky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * <p>Project: rikky-takeaway - RikkyApplication
 * <p>Powered by Riverify On 12-14-2022 23:28:13
 * <p><a href="https://github.com/Riverify/rikky-takeaway">该项目的github仓库</a>
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan  // 扫描 Servlet、Filter、Listener 确保过滤器生效
public class RikkyApplication {
    public static void main(String[] args) {
        SpringApplication.run(RikkyApplication.class, args);
        log.info("项目启动成功! [悦刻外卖 version: 1.1.2] https://github.com/Riverify/rikky-takeaway");
        log.info("后台管理系统默认链接: http://localhost:8080/backend/index.html");
        log.info("前台管理系统默认链接: http://localhost:8080/front/index.html");
    }
}
