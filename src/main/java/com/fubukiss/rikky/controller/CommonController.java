package com.fubukiss.rikky.controller;

import com.fubukiss.rikky.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * <p>Project: rikky-takeaway - CommonController 公共Controller类
 * <p>Powered by river On 2023/01/09 7:56 PM
 *
 * @author Riverify
 * @version 1.0
 * @since JDK8
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    /**
     * file将会存放的位置，通过@Value注解获取配置文件中的值
     */
    @Value("${rikky.linux-path}")
    private String basePath;


    /**
     * 文件上传
     *
     * @param file 文件，file是一个临时文件，需要将其转存到指定目录，否则会被删除
     * @return 通用返回对象
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {  // ！！！参数名必须为与前端的请求name保持一致
        log.info("文件上传:{}", file.toString());

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用UUID生成文件名
        String fileName = UUID.randomUUID() + suffix;
        // 创建一个目录，判断是否存在，不存在则创建
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName)); // 将文件转存到指定目录
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 返回文件名
        return R.success(fileName);
    }


    /**
     * 文件下载
     *
     * @param name     文件名
     * @param response 存放文件的response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        FileInputStream fileInputStream = null;     // 文件输入流
        ServletOutputStream outputStream = null;    // 输出流

        try {
            // 输入流，通过输入读取文件内容
            fileInputStream = new FileInputStream(new File(basePath + name));
            // 输出流，通过输出将文件写回浏览器，在浏览器展示图片
            outputStream = response.getOutputStream();
            // 读取文件内容，写回浏览器
            byte[] bytes = new byte[1024];  // 1KB
            int length = 0;                 // 每次读取的长度
            while ((length = fileInputStream.read(bytes)) != -1) {  // 读取文件内容
                outputStream.write(bytes, 0, length);           // 将文件内容写回浏览器
                outputStream.flush();                               // 刷新缓冲区
            }

            // 设置响应头，告诉浏览器以图片的形式打开
            response.setContentType("image/jpeg");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
