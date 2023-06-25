package com.wang.scaffold.user.auth.interceptor;

import com.wang.scaffold.sharded.exception.VersionException;
import com.wang.scaffold.user.auth.details.AbstractAuthenticationDetails;
import com.wang.scaffold.utils.ComparableVersion;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 手机app版本，低版本拦截器
 */
public class ClientAppVersionInterceptor implements PreAuthenInterceptor {
    final String minimumVersionString = "1.3.0";

    @Override
    public void preAuthenticate(Authentication authenToken) throws AuthenticationException {
        Object details = authenToken.getDetails();
        if(details instanceof AbstractAuthenticationDetails) {
            AbstractAuthenticationDetails authenticationDetails = (AbstractAuthenticationDetails) details;
            if(authenticationDetails.getClientInfo().isApp()) {
                String appVersion = authenticationDetails.getClientInfo().getAppVersion();
                final String exMsg = authenToken.getPrincipal().toString() + ":您使用的应用版本过旧，无法登录，请更新至新版本。";
                if(appVersion != null) {
                    // 拦截版本号为minimumVersionString之前的登录
                    ComparableVersion clientVersion = new ComparableVersion(appVersion);
                    ComparableVersion minimumVersion = new ComparableVersion(minimumVersionString);
                    if (minimumVersion.compareTo(clientVersion) > 0) {
                        throw new VersionException(exMsg);
                    }
                } else {
                    throw new VersionException(exMsg);
                }
            }
        }
    }
}
