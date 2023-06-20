package com.wang.scaffold.entity;

/**
 * 用于VO等数据传输对象实现此接口，使用delegate模式
 * @param <T> Entity类型
 * @author weizhou
 */
public interface EntityDelegate<T> {

    /**
     * 方便从VO直接获取entity
     * @return the delegate entity
     */
    T entity();
}
