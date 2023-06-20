package com.wang.scaffold.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 上传文件操作
 * @author weizhou
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UploadFileAction {

	FileEntityActionType actionType() default FileEntityActionType.MERGE;

	/**
	 * parameter或者return
	 */
	String proxySources() default "parameter";
}
