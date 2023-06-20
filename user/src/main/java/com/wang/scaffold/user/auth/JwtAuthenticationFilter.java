package com.wang.scaffold.user.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.scaffold.consts.WebConstants;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.sharded.exception.CaptchaException;
import com.wang.scaffold.sharded.exception.PhoneNotFoundException;
import com.wang.scaffold.sharded.exception.VersionException;
import com.wang.scaffold.sharded.security.JwtProperties;
import com.wang.scaffold.sharded.security.token.JwtTokenGenerator;
import com.wang.scaffold.sharded.security.token.TokenResponse;
import com.wang.scaffold.sharded.user.LoginUser;
import com.wang.scaffold.user.auth.details.AbstractAuthenticationDetails;
import com.wang.scaffold.user.auth.interceptor.PreAuthenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录校验
 *
 * @author zhou wei
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final List<PreAuthenInterceptor> preAuthenInterceptors = new ArrayList<PreAuthenInterceptor>();
    private JwtProperties jwtProperties;
    private AuthTokenService authTokenService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, String url) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(url);
    }

    public void addPreAuthenInterceptor(PreAuthenInterceptor interceptor) {
        preAuthenInterceptors.add(interceptor);
    }

    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public void setAuthTokenService(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        Authentication authToken = AuthenticationTokenFactory.buildAuthToken(request);
        if (authToken == null) {
            return null;
        }
        for (PreAuthenInterceptor preAuthenInterceptor : preAuthenInterceptors) {
            preAuthenInterceptor.preAuthenticate(authToken);
        }
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        // 账号已被停用
        if (failed instanceof DisabledException || failed instanceof LockedException) {
            this.sendResponse(response, TokenResponse.accountDisabled());
        }
        // 密码错误
        else if (failed instanceof BadCredentialsException) {
            this.sendResponse(response, TokenResponse.wrongPassword());
        }
        // 用户名没找到
        else if (failed instanceof UsernameNotFoundException) {
            this.sendResponse(response, TokenResponse.wrongPassword());
        } else if (failed instanceof PhoneNotFoundException) {
            this.sendResponse(response, TokenResponse.fail(WebConstants.PHONE_NOT_FOUND, failed.getMessage()));
        }
        // 验证码异常
        else if (failed instanceof CaptchaException) {
            this.sendResponse(response, TokenResponse.fail(WebConstants.CAPTCHA_EX, failed.getMessage()));
        }
        // app版本过旧
        else if (failed instanceof VersionException) {
            this.sendResponse(response, TokenResponse.fail(WebConstants.APP_OUTDATED, failed.getMessage()));
        }
        // 其他
        else {
            log.error(failed.getMessage());
            this.sendResponse(response, TokenResponse.fail());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authResult) {
        Assert.isInstanceOf(LoginUser.class, authResult.getPrincipal(), "Only LoginUser is supported");
        Assert.isInstanceOf(AbstractAuthenticationDetails.class, authResult.getDetails(), "Only AbstractAuthenticationDetails is supported");

        SecurityContextHolder.getContext().setAuthentication(authResult);

        LoginUser user = ((LoginUser) authResult.getPrincipal());
        AbstractAuthenticationDetails details = (AbstractAuthenticationDetails) authResult.getDetails();

        String token = TokenUtils.genertateToken(JwtTokenGenerator.withProperties(jwtProperties), user);
        String refreshToken = authTokenService.createRefreshToken(user.getUsername(), details.getClientInfo());
        user.setToken(token);
        user.setRefreshToken(refreshToken);

        // Fire event
        if (super.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                    authResult, this.getClass()));
        }
        this.sendResponse(response, TokenResponse.success(user));
    }

    private void sendResponse(HttpServletResponse response, BaseResponse<?> res) {
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        try (PrintWriter pw = response.getWriter()) {
            ObjectMapper mapper = new ObjectMapper();
            String responseStr = mapper.writeValueAsString(res);
            pw.write(responseStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
