package com.wang.scaffold.sharded.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.scaffold.response.BaseResponse;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.sharded.security.token.Constants;
import com.wang.scaffold.sharded.security.token.JwtTokenParser;
import com.wang.scaffold.sharded.security.token.ParsedJwtToken;
import com.wang.scaffold.sharded.security.token.TokenResponse;
import com.wang.scaffold.sharded.user.RolePermissionCacheDao;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * OncePerRequestFilter子类，用于校验http请求头中的jwt token，
 * 校验并解析token后新建一个UsernamePasswordAuthenticationToken，放入SecurityContext中
 * <br><br>
 * 请求头是Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ...
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtProperties jwtProperties;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String token = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(token) && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                List<SimpleGrantedAuthority> authorities = null;

                ParsedJwtToken pjt = JwtTokenParser.useSecret(jwtProperties.getSecret()).parseToken(token);
                String username = pjt.getUsername();
                List<Integer> roleIds = pjt.getClaimList(Constants.GROUPS, o -> Integer.valueOf(o.toString()));
                if (roleIds != null) {
                    authorities = new ArrayList<>();
                    RolePermissionCacheDao cache = WebAppContextHelper.getBean(RolePermissionCacheDao.class); // 获取Spring容器中已初始化的bean
                    assert cache != null;
                    Set<String> perms = cache.getPermsByRole(roleIds);
                    for (String perm : perms) {
                        authorities.add(new SimpleGrantedAuthority(perm));
                    }
                }
                List<String> perms = pjt.getClaimList(Constants.PERMS, Object::toString);
                if (perms != null) {
                    if (authorities == null) authorities = new ArrayList<>();
                    authorities.addAll(perms.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                }
                if (!StringUtils.isEmpty(username)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    authentication.setDetails(ClientInfoUtil.extraClientInfo(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                }
            } catch (ExpiredJwtException exception) {
                this.sendResponse(response, TokenResponse.expire());
            } catch (UnsupportedJwtException exception) {
                log.warn("不支持的token格式 JWT : {} failed : {}", token, exception.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (MalformedJwtException exception) {
                log.warn("无效的token JWT : {} failed : {}", token, exception.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (SignatureException exception) {
                log.warn("Token签名错误 : {} failed : {}", token, exception.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (IllegalArgumentException exception) {
                log.warn("无token JWT : {} failed : {}", token, exception.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            filterChain.doFilter(request, response);
        }
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
