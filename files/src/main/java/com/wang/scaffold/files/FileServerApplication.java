package com.wang.scaffold.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName FilesApplication.java
 * @Description TODO
 * @createTime 2023年06月30日 16:35:00
 */
@EnableAsync
@SpringBootApplication(scanBasePackages = "com.wang.scaffold")
public class FileServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class,args);
    }
}
