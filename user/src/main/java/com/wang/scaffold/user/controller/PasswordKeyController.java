package com.wang.scaffold.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.sharded.security.SensitiveInfoAccess;
import com.wang.scaffold.user.service.UserService;
import com.wang.scaffold.utils.AesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/passkey")
public class PasswordKeyController {

    @Autowired
    private SensitiveInfoAccess sensitiveInfoAccess;
    @Autowired
    private UserService userService;

    /**
     * 通过类似登录接口的用户名密码来获取aes密钥
     * @param encryptedStr 前端aes加密后的登录json
     * @param timestamp encryptedStr解密aes密钥
     */
    @PostMapping(path = "/gen")
    public BaseResponse<?> generateMyPasskey(@RequestBody String encryptedStr, @RequestParam String timestamp) {
        try {
            String loginJson = AesUtils.decrypt(encryptedStr, timestamp);
            ObjectMapper mapper = new ObjectMapper();
            String username = mapper.readTree(loginJson).get("username").asText();
            String password = mapper.readTree(loginJson).get("password").asText();
            UserDetails user = userService.loadUserByUsername(username);
            String pwdDigest = user.getPassword();
            PasswordEncoder passwordEncoder = WebAppContextHelper.getBean(PasswordEncoder.class);
            assert passwordEncoder != null;
            if (passwordEncoder.matches(password, pwdDigest)) {
                Map<String, Object> result = new HashMap<>();
                String aesPassword = sensitiveInfoAccess.findMyAesPassword();
                result.put("password", aesPassword);
                result.put("timeout", sensitiveInfoAccess.resetExpiration());
                return BaseResponse.success(result);
            } else {
                return BaseResponse.fail("密码错误");
            }
        } catch (GeneralSecurityException | JsonProcessingException e) {
            return BaseResponse.fail(e.getMessage());
        }
    }
}
