package com.fubukiss.rikky;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>Project: rikky-takeaway - RikkyApplication
 * <p>Powered by Riverify On 12-14-2022 23:28:13
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@Slf4j
@SpringBootApplication
public class RikkyApplication {
    public static void main(String[] args) {
        SpringApplication.run(RikkyApplication.class, args);
        log.info("项目启动成功 [悦刻外卖 version: 1.0-SNAPSHOT]");
    }
}
