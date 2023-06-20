package com.wang.scaffold.entity.mybatis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName BaseEntity.java
 * @Description TODO
 * @createTime 2023年06月20日 10:36:00
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 创建人
    @TableField(fill = FieldFill.INSERT)
    private String createdBy;
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
    // 更新人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;
}
