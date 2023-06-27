package com.wang.scaffold.user.controller;

import com.wang.scaffold.annotation.SysLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName TestController.java
 * @Description TODO
 * @createTime 2023年06月19日 16:21:00
 */
@RestController
@RequestMapping("Hello")
public class HelloController {

    @SysLog(module = "测试管理", operation = "查询")
    @GetMapping("/world")
    public String hello(){
        return "Hello world";
    }
}
