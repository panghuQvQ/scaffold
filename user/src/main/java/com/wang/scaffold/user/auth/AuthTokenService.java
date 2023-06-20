package com.wang.scaffold.user.auth;

import com.wang.scaffold.annotation.SysLog;
import com.wang.scaffold.consts.UserDevice;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.sharded.security.JwtProperties;
import com.wang.scaffold.sharded.security.token.JwtTokenGenerator;
import com.wang.scaffold.sharded.user.ClientInfo;
import com.wang.scaffold.sharded.user.LoginUser;
import com.wang.scaffold.user.entity.AuthToken;
import com.wang.scaffold.user.repository.AuthTokenRepo;
import com.wang.scaffold.user.service.UserActiveLogService;
import com.wang.scaffold.user.service.impl.UserServiceImpl;
import com.wang.scaffold.utils.CryptoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountLockedException;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthTokenService {

    @Autowired
    AuthTokenRepo authTokenRepo;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    JwtProperties jwtProperties;
    @Autowired
    UserActiveLogService userActiveLogService;

    /**
     * 刷新token，数据库里储存uuid，用jwt的secret作为key，将uuid和时间戳、jwt secret拼接，使用HMACSHA256算出签名。
     *
     * @param username 账号
     * @param clientInfo 客户端信息
     * @return Base64(uuid + " . " + timestamp + " . " + sign)
     */
    @SysLog(module = "登录", operation = "登录日志", ignoreResponse = true)
    @Transactional
    public String createRefreshToken(String username, ClientInfo clientInfo) {
        UserDevice device = clientInfo.getDevice();
        Date expireTime = clientInfo.isApp()
                ? new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpireMobile())
                : new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpire());
        AuthToken authToken = new AuthToken();
        authToken.setExpireTime(expireTime);
        authToken.setUsername(username);
        authToken.setDevice(device.getDeviceName());
        authToken.setUserAgent(clientInfo.getUserAgent());
        authToken.setRefreshToken(UUID.randomUUID().toString());
        authTokenRepo.save(authToken);
        userActiveLogService.log(authToken.getUsername()); // 每次创建/刷新Token记录用户活跃日志
        authTokenRepo.deleteExpiredToken(username, new Date());
        return genEcodedToken(authToken);
    }

    private String genEcodedToken(AuthToken authToken) {
        String refreshTokenId = authToken.getRefreshToken();
        long timestamp = authToken.getExpireTime().getTime();
        String sign = null;
        try {
            sign = CryptoUtils.signWithHMACSHA256(refreshTokenId + timestamp + jwtProperties.getSecret(), jwtProperties.getSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String rawToken = refreshTokenId + "." + timestamp + "." + sign;
        return Base64.getUrlEncoder().encodeToString(rawToken.getBytes());
    }

    /**
     * 刷新token的具体实现。
     * 通过refresh token从数据库取出，更新token和refresh token，保存，返回，
     * 最后删除当前用户的过期token数据。
     */
    @Transactional
    public LoginUser refreshToken(String rtoken) throws UnderAttactException, AccountLockedException {
        String decodedToken = new String(Base64.getUrlDecoder().decode(rtoken));
        String[] tempStrs = decodedToken.split("\\.");
        try {
            String sign = CryptoUtils.signWithHMACSHA256(tempStrs[0] + tempStrs[1] + jwtProperties.getSecret(), jwtProperties.getSecret());
            if (!tempStrs[2].equals(sign)) {
                throw new SignatureException("bad refresh token");
            }
        } catch (Exception e) {
            throw new BadTokenException("bad token");
        }
        AuthToken authToken = authTokenRepo.findByRefreshToken(tempStrs[0], new Date());
        ClientInfo clientInfo = WebAppContextHelper.currentClientInfo();
        if (authToken != null && clientInfo != null) {
            long expireTimeDb = authToken.getExpireTime().getTime();
            long expireTimeTk = Long.parseLong(tempStrs[1]);
            if (expireTimeTk != expireTimeDb) {
                // 上一次请求是使用过期token，触发自动刷新token，但是还没返回给前端，第二次请求又过来，数据库里已经更新过期时间，
                // 当发生这样的情况时，保留一定的时间差内都有效（暂定10秒）
                long timeGap = System.currentTimeMillis() -
                        (expireTimeDb - (clientInfo.isApp() && authToken.isMobile()
                                ? jwtProperties.getRefreshTokenExpireMobile()
                                : jwtProperties.getRefreshTokenExpire()));
                if (timeGap < 10000) {
                    log.debug("连续请求刷新token发生，时间间隔{}毫秒，这通常是在网络或服务器高延迟时候发生。", timeGap);
                } else {
                    // 为保护安全，删除有泄露风险的token
                    authTokenRepo.delete(authToken);
                    log.debug("AuthToken:{}", authToken);
                    log.debug("decodedToken:{}", decodedToken);
                    throw new UnderAttactException("账号可能被攻击，数据传输有被泄露风险");
                }
            }
            LoginUser loginUser = userService.loadUserByUsername(authToken.getUsername());
            if (!loginUser.isAccountNonLocked()) {
                throw new AccountLockedException();
            }

            String token = TokenUtils.genertateToken(JwtTokenGenerator.withProperties(jwtProperties), loginUser);
            Date nDate = new Date(System.currentTimeMillis() +
                    (clientInfo.isApp() && authToken.isMobile()
                            ? jwtProperties.getRefreshTokenExpireMobile()
                            : jwtProperties.getRefreshTokenExpire()));

            authToken.setExpireTime(nDate);
            authToken.setUserAgent(clientInfo.getUserAgent());
            authTokenRepo.save(authToken);
            userActiveLogService.log(authToken.getUsername()); // 每次创建/刷新Token记录用户活跃日志

            loginUser.setRefreshToken(genEcodedToken(authToken));
            loginUser.setToken(token);
            return loginUser;
        } else {
            throw new BadTokenException("登录已过期");
        }
    }

    @Transactional
    public void deleteToken(String rtoken) {
        String decodedToken = new String(Base64.getUrlDecoder().decode(rtoken));
        String[] tempStrs = decodedToken.split("\\.");
        Optional<AuthToken> op = authTokenRepo.findOne((root, query, cb) -> cb.equal(root.get("refreshToken").as(String.class), tempStrs[0]));
        op.ifPresent(e -> authTokenRepo.delete(e));
    }

}
