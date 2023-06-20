package com.wang.scaffold.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 隐藏原始id，对原始id的处理必须是双向encode及decode
 * @param <R> 隐藏后的id类型
 * @param <T> 原始id类型
 * @author weizhou
 */
public interface MaskId<R, T> {
    @JsonIgnore
    T getId();
    void setId(T id);

    IdEncoder<R, T> idEncoder();

    default R get_id() {
        return idEncoder().encode(getId());
    }
    default void set_id(R encodedId) {
        if(getId() == null && encodedId != null) {
            T id = idEncoder().decode(encodedId);
            setId(id);
        }
    }

    interface IdEncoder<R, T> {
        R encode(T id);
        T decode(R encodedId);
    }
}
