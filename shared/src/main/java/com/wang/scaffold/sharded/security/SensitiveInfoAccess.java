package com.wang.scaffold.sharded.security;

import com.wang.scaffold.consts.RedisKeys;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Lazy
@Component
public class SensitiveInfoAccess {

    public static final Duration DEFAULT_EXPIRE_DURATION = Duration.ofMinutes(30);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 从redis获取我的aes密钥，如果没有，则生成一个
     * @return aesPassword
     */
    public String findMyAesPassword() {
        String username = Objects.requireNonNull(WebAppContextHelper.currentUsername());
        String redisKey = RedisKeys.SENSITIVE_INFO_ACCESS_PREFIX + username;
        BoundValueOperations<String, String> redisOps = redisTemplate.boundValueOps(redisKey);
        String aesPassword = redisOps.get();
        if (aesPassword == null) {
            aesPassword = CryptoUtils.generate32bytesKey();
            redisOps.set(aesPassword);
        }
        return aesPassword;
    }

    /**
     * 重置过期时间
     * @return 过期时间的时间戳
     */
    public long resetExpiration() {
        String username = Objects.requireNonNull(WebAppContextHelper.currentUsername());
        String redisKey = RedisKeys.SENSITIVE_INFO_ACCESS_PREFIX + username;
        redisTemplate.expire(redisKey, DEFAULT_EXPIRE_DURATION);
        return System.currentTimeMillis() + DEFAULT_EXPIRE_DURATION.toMillis();
    }

    public static String desensitizeIDNo(String idNo) {
        if (idNo == null || idNo.length() != 18) {
            return idNo;
        }
        return idNo.substring(0, 4) + "**********" + idNo.substring(14);
    }

    public static String desensitizePhoneNo(String phoneNo) {
        if (phoneNo == null || phoneNo.length() < 11) {
            return phoneNo;
        }
        StringBuilder sb = new StringBuilder();
        if (phoneNo.charAt(0) == '0') { // 座机
            if (phoneNo.contains("-")) {
                sb.append(phoneNo, 0, phoneNo.indexOf("-") + 3);
            } else {
                sb.append(phoneNo,0, 6);
            }
        } else { // 座机外的、手机
            sb.append(phoneNo,0, 3);
        }
        int xCount = phoneNo.length() - 2 - sb.length();
        sb.append("******************", 0, xCount);
        sb.append(phoneNo.substring(phoneNo.length() - 2));
        return sb.toString();
    }

    public static String desensitizeBankAccountNo(String bankAccountNo) {
        if (bankAccountNo.length() < 4) {
            return bankAccountNo;
        }
        return "**** " + bankAccountNo.substring(bankAccountNo.length() - 4);
    }
}
