package com.wang.scaffold.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wang zhen xing
 * @date 2020/6/5 17:31
 * @desc 系统操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
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
