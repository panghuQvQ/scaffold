package com.wang.scaffold.sharded.file;

import com.wang.scaffold.annotation.FileEntityActionType;
import com.wang.scaffold.annotation.UploadFileAction;
import com.wang.scaffold.file.UploadFileEntity;
import com.wang.scaffold.file.support.SimpleUploadFileEntity;
import com.wang.scaffold.sharded.feign.IFilesFeign;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class UploadFileAspect {

	@Autowired(required = false)
	private IFilesFeign filesFeign;

	@Pointcut("@annotation(com.wang.scaffold.annotation.UploadFileAction)")
	public void fileActionPointCut() {

	}

	@AfterReturning(pointcut = "fileActionPointCut()", returning = "response")
	public void persistUploadFileAction(JoinPoint joinPoint, Object response) {
		if (filesFeign == null) {
			log.error("未配置feign，无法与文件服务通信。");
			return;
		}
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		UploadFileAction fileAction = method.getAnnotation(UploadFileAction.class);

		List<UploadFileEntity> ufes = new ArrayList<>();

		String proxySources = fileAction.proxySources();
		if ("parameter".equalsIgnoreCase(proxySources)) {
			Object[] args = joinPoint.getArgs();
			for (Object object : args) {
				this.convertAndAdd(object, ufes);
			}
		} else if("return".equalsIgnoreCase(proxySources)) {
			this.convertAndAdd(response, ufes);
		}

		if (ufes.size() == 0) {
			if ("return".equalsIgnoreCase(proxySources) && response != null) {
				log.error("没有获取到UploadFileEntity，请检查返回值是否实现了UploadFileEntity。");
			} else {
				log.error("没有获取到UploadFileEntity，请检查注解配置是否正确。");
			}
			return;
		}
		List<SimpleUploadFileEntity> request = ufes.stream().map(SimpleUploadFileEntity::new).collect(Collectors.toList());
		if (fileAction.actionType() == FileEntityActionType.MERGE) {
			filesFeign.merge(request);
		}
		if (fileAction.actionType() == FileEntityActionType.REMOVE) {
			filesFeign.remove(request);
		}
	}

	private void convertAndAdd(Object object, List<UploadFileEntity> ufes) {
		if(object == null) return;
		if (object instanceof UploadFileEntity) {
			UploadFileEntity ufe = (UploadFileEntity) object;
			ufes.add(ufe);
		}
		if (object instanceof Collection) {
			@SuppressWarnings("rawtypes")
			Collection temp = (Collection) object;
			for (Object item : temp) {
				convertAndAdd(item, ufes);
			}
		}
		if (object instanceof UploadFileEntity[]) {
			UploadFileEntity[] temp = (UploadFileEntity[]) object;
			for (Object item : temp) {
				UploadFileEntity ufe = (UploadFileEntity) item;
				ufes.add(ufe);
			}
		}
	}
}
