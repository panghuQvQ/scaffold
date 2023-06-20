package com.wang.scaffold.user.dto.vo;


import com.wang.scaffold.entity.EntityDelegate;
import com.wang.scaffold.entity.HashIdEncoder;
import com.wang.scaffold.entity.MaskId;
import com.wang.scaffold.user.entity.Permission;
import com.wang.scaffold.user.entity.Role;

import java.util.List;

public class RoleVO implements MaskId<String, Integer>, EntityDelegate<Role> {

    private final Role role;

    public RoleVO() {
        role = new Role();
    }

    public RoleVO(Role role) {
        this.role = role;
    }

    @Override
    public Role entity() {
        return role;
    }

    @Override
    public Integer getId() {
        return role.getId();
    }

    @Override
    public void setId(Integer id) {
        role.setId(id);
    }

    public String getName() {
        return role.getName();
    }

    public void setPermissions(List<Permission> permissions) {
        role.setPermissions(permissions);
    }

    public List<Permission> getPermissions() {
        return role.getPermissions();
    }

    public void setName(String name) {
        role.setName(name);
    }

    @Override
    public String toString() {
        return role.toString();
    }

    @Override
    public boolean equals(Object o) {
        return role.equals(o);
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public IdEncoder<String, Integer> idEncoder() {
        return HashIdEncoder.getInstance(getClass().getSimpleName(), 4);
    }
}
