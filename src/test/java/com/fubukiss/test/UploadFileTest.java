package com.fubukiss.test;

import org.junit.jupiter.api.Test;

/**
 * <p>Project: rikky-takeaway - UploadFileTest 文件上传测试类
 * <p>Powered by river On 2023/01/10 9:53 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
public class UploadFileTest {

    @Test
    public void test1() {
        String fileName = "114514.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }
}
