package com.wang.scaffold.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 移动客户端适配api
 * @author weizhou
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AppClientApi {

    /**
     * 要求的最低app版本
     */
    String minimumVersion() default "0";

    /**
     *
     */
    String lowVersionMessage() default "";
}
