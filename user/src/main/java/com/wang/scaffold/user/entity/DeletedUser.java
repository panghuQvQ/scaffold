package com.wang.scaffold.user.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 已删除用户
 */
@ToString(exclude = "password")
@Data
@Entity
@Table(name = "app_deleted_user")
public class DeletedUser implements Serializable {
    @Id
    private Integer id;
    private String username;
    private String name;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    private String password;
    /**
     * 账号禁用
     */
    private boolean disabled;
    /**
     * 角色id, 用,分隔
     */
    private String roleIds;

    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    /**
     * 删除时间
     */
    @Column(nullable = false, updatable = false)
    private Date deletedTime;
    /**
     * 删除原因
     */
    @Column(length = 500)
    private String deleteReason;
    /**
     * 删除人
     */
    @Column(updatable = false)
    private String deletedBy;
}
