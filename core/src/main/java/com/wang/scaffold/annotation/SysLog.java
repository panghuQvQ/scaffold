package com.wang.scaffold.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title 自定义注解
 * @description 系统操作日志
 * @author wzy
 * @updateTime 2023/6/27 11:07
 * @throws
 */
@Target(ElementType.METHOD) // @Target说明了Annotation所修饰的对象范围：方法
@Retention(RetentionPolicy.RUNTIME) // @Retention定义了该Annotation被保留的时间长短：在运行时有效（即运行时保留）
public @interface SysLog {
    /**
     * 操作模块
     */
    String module() default "";
    /**
     *  操作内容
     */
    String operation() default "";

    /**
     * 不记录返回值，默认false，记录返回值。
     */
    boolean ignoreResponse() default false;

    /**
     * 不记录的参数，默认全记录。配置参数名称。
     */
    String[] ignoreParameters() default {};

    /**
     * 参数/返回值存储形式，两种形式："toString" "json".默认json
     */
    String serializationType() default "json";
}
