package com.wang.scaffold.files.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 受保护文件读取拦截器，
 * 读取受保护文件时，需要提供一个临时code，拦截器根据code去redis里查询code对应的文件名，
 * 对比是否是此次请求的文件，来判断时候有访问权限。
 */
@Component
public class ProtectedFileInterceptor implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String accessCode = request.getParameter("code");
        String filenameInRedis = null;
        if (accessCode != null) {
            filenameInRedis = redisTemplate.opsForValue().get(accessCode);
        }
        String sbUrl = request.getRequestURL().toString();
        sbUrl = java.net.URLDecoder.decode(sbUrl, "utf-8");
        String filename = sbUrl.substring(sbUrl.lastIndexOf("/") + 1);
        if (accessCode == null || !filename.equals(filenameInRedis)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else {
//				redisTemplate.delete(accessCode);
            return true;
        }
    }

}
