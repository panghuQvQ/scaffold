package com.wang.scaffold.user.entity;

import com.wang.scaffold.entity.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 用户收藏
 */
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "app_user_favorite")
public class UserFavorite extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    /** 用户名 */
    private String username;
    /** 一级分类 */
    private String category;
    /** 二级分类 */
    private String secondaryCategory;
    /** 跳转路由 */
    private String route;
    /** 资源唯一键 */
    private String resourceId;
    /** 值 */
    private String details;
}
