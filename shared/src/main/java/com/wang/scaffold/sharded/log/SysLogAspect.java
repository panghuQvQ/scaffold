package com.wang.scaffold.sharded.log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.scaffold.annotation.SysLog;
import com.wang.scaffold.entity.OperationLog;
import com.wang.scaffold.sharded.helper.WebAppContextHelper;
import com.wang.scaffold.utils.userAgent.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Aspect
@Component
public class SysLogAspect {

	@Autowired(required = false)
	private DataSource dataSource;

	private ObjectMapper mapper = new ObjectMapper();

	{
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	/** 异步记录日志 */
	private static ExecutorService logExecutorService = Executors.newSingleThreadExecutor();

	@Pointcut("@annotation(com.byx.scaffold.annotation.SysLog)")
	public void logPointCut() {

	}

	@AfterReturning(pointcut = "logPointCut()", returning = "response")
	public void saveSysLog(JoinPoint joinPoint, Object response) {
		if (dataSource == null) {
			log.error("未找到DataSource，无法记录操作日志。");
			return;
		}
		// The method of obtaining the weaving point from the cut in point by reflection mechanism
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		// Gets the requested class name
		String className = joinPoint.getTarget().getClass().getName();
		// Gets the requested method name
		String methodName = method.getName();
		methodName = className + "." + methodName;

		String operation = null;
		// get Annotation metadata
		SysLog sysLog = method.getAnnotation(SysLog.class);
		if(sysLog == null) return; // Could not happen if the point cut sets correctly.
		operation = "[" + sysLog.module() + "]" + sysLog.operation();

		// param info
		StringJoiner sjArgsInfo = new StringJoiner(",");
		Parameter[] parameters = method.getParameters();
		Object[] args = joinPoint.getArgs();

		outer:
			for (int i = 0; i < args.length; i++) {
				try {
					String paramName = parameters[i].getName();
					String[] ignoreParameters = sysLog.ignoreParameters();
					if (ignoreParameters.length > 0) {
						for (String ignore : ignoreParameters) {
							if(paramName.equals(ignore)) {
								continue outer;
							}
						}
					}
					String argInfo = null;
					if ("toString".equalsIgnoreCase(sysLog.serializationType())) {
						argInfo = args[i].toString();
					} else {
						argInfo = mapper.writeValueAsString(args[i]);
					}
					sjArgsInfo.add(paramName + ":" + argInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		// return info
		String returnInfo = null;
		if (!sysLog.ignoreResponse()) {
			try {
				if ("toString".equalsIgnoreCase(sysLog.serializationType())) {
					returnInfo = response.toString();
				} else {
					returnInfo = mapper.writeValueAsString(response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Get RequestAttributes
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		// Get the information of HttpServletRequest from get RequestAttributes
		HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
		String ip = UserAgentUtils.getIpAddr(request);
		String userAgent = UserAgentUtils.getUserAgentString(request);

		String uername = WebAppContextHelper.currentUsername();
		OperationLog operationLog = new OperationLog(null, operation, methodName,
				sjArgsInfo.length() > 0 ? sjArgsInfo.toString() : null, returnInfo,
						null, uername, ip, userAgent);
		logExecutorService.execute(() -> {
			recordOperation(operationLog);
		});
	}

	public void recordOperation(OperationLog log) {
		String sql = "INSERT INTO operation_log (operation, request_method, parameters, response, operate_time, operator, ip, user_agent)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = dataSource.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setString(1, log.getOperation());
			pst.setString(2, log.getRequestMethod());
			pst.setString(3, log.getParameters());
			pst.setString(4, log.getResponse());
			pst.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			pst.setString(6, log.getOperator());
			pst.setString(7, log.getIp());
			pst.setString(8, log.getUserAgent());
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
