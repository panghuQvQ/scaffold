package com.wang.scaffold.sharded;

import com.wang.scaffold.annotation.AppClientApi;
import com.wang.scaffold.sharded.exception.VersionException;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.sharded.user.ClientInfo;
import com.wang.scaffold.utils.ComparableVersion;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class AppClientVersionAspect {

	private static final String DEFAULT_MSG = "您的应用版本过低，无法使用此功能，请更新。从底部导航【我的->版本信息->版本更新】可以检查更新。";

    @Before("@annotation(com.wang.scaffold.annotation.AppClientApi)")
    public void appClientVersionCheck(JoinPoint joinPoint) {
        // The method of obtaining the weaving point from the cut in point by reflection mechanism
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // Gets the requested class name
        String className = joinPoint.getTarget().getClass().getName();
        // Gets the requested method name
        String methodName = method.getName();
        methodName = className + "." + methodName;
        AppClientApi appClientApi = method.getAnnotation(AppClientApi.class);
        if (appClientApi == null) return; // Could not happen if the point cut sets correctly.
        String requiredMinimum = appClientApi.minimumVersion();
        ClientInfo clientInfo = WebAppContextHelper.currentClientInfo();
        if (clientInfo != null && clientInfo.isApp()) {
            String client = clientInfo.getAppVersion();
            if (clientInfo.getAppVersion() == null) {
                client = "0";
            }
            ComparableVersion minimumVersion = new ComparableVersion(requiredMinimum);
            ComparableVersion clientVersion = new ComparableVersion(client);
            if (minimumVersion.compareTo(clientVersion) > 0) {
                String currentUsername = WebAppContextHelper.currentUsername();
                if (currentUsername == null) currentUsername = "Anonymous";
                log.warn("[{}]的应用版本为({}),{}方法要求的最低版本为({})", currentUsername, client, methodName, requiredMinimum);
                String msg = "".equals(appClientApi.lowVersionMessage())
                        ? DEFAULT_MSG
                        : appClientApi.lowVersionMessage();
                throw new VersionException(msg);
            }
        }
    }
}
