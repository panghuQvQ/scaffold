package com.wang.scaffold.sharded.security;

import com.wang.scaffold.sharded.user.ClientInfo;
import com.wang.scaffold.utils.userAgent.UserAgentUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public abstract class ClientInfoUtil {
    public static ClientInfo extraClientInfo(HttpServletRequest request) {
        String ua = UserAgentUtils.getUserAgentString(request);
        if (!StringUtils.hasText(ua)) return null;

        ClientInfo clientInfo = new ClientInfo();

        if (ua.contains("Windows")) {
            clientInfo.setOs("Windows");
        } else if (ua.contains("Macintosh")) {
            clientInfo.setOs("Mac OS");
        } else if (ua.contains("ios") || ua.contains("iPhone")) {
            clientInfo.setOs("iOS");
        } else if (ua.toLowerCase().contains("android")) {
            clientInfo.setOs("Android");
        } else if (ua.contains("Linux")) {
            clientInfo.setOs("Linux");
        } else {
            clientInfo.setOs("Unknown");
        }

        if (ua.startsWith("Mozilla/5.0")) {
            if (ua.contains("Edg/")) {
                clientInfo.setRuntime("Edge");
            } else {
                clientInfo.setRuntime(UserAgentUtils.getBrowser(request).getName());
            }
        } else if (ua.startsWith("Dart/")) {
            clientInfo.setRuntime(ua.substring(0, ua.indexOf(" ")));
            if (ua.contains("app/")) {
                clientInfo.setAppVersion(ua.substring(ua.indexOf("app/") + 4));
            }
        } else {
            clientInfo.setRuntime("Unknown");
        }

        clientInfo.setIpv4(UserAgentUtils.getIpAddr(request));
        clientInfo.setUserAgent(UserAgentUtils.getUserAgentString(request));
        return clientInfo;
    }
}
