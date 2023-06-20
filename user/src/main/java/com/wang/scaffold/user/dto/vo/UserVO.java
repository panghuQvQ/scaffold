package com.wang.scaffold.user.dto.vo;



import com.wang.scaffold.entity.EntityDelegate;
import com.wang.scaffold.entity.HashIdEncoder;
import com.wang.scaffold.entity.MaskId;
import com.wang.scaffold.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserVO implements MaskId<String, Integer>, EntityDelegate<User> {

    private final User user;

    public UserVO() {
        user = new User();
    }

    public UserVO(User user) {
        this.user = user;
    }

    @Override
    public User entity() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public String getName() {
        return user.getName();
    }

    public String getAvatar() {
        return user.getAvatar();
    }

    public String getPhone() {
        return user.getPhone();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public boolean isDisabled() {
        return user.isDisabled();
    }

    public List<RoleVO> getRoles() {
        if (user.getRoles() == null) return null;
        return user.getRoles().stream().map(RoleVO::new).collect(Collectors.toList());
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public void setName(String name) {
        user.setName(name);
    }

    public void setAvatar(String avatar) {
        user.setAvatar(avatar);
    }

    public void setPhone(String phone) {
        user.setPhone(phone);
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    public void setPassword(String password) {
        user.setPassword(password);
    }

    public void setDisabled(boolean disabled) {
        user.setDisabled(disabled);
    }

    public void setRoles(List<RoleVO> roles) {
        if (roles != null)
        user.setRoles(roles.stream().map(RoleVO::entity).collect(Collectors.toList()));
    }

    @Override
    public boolean equals(Object o) {
        return user.equals(o);
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }

    @Override
    public String toString() {
        return user.toString();
    }

    @Override
    public Integer getId() {
        return user.getId();
    }

    @Override
    public void setId(Integer id) {
        user.setId(id);
    }

    @Override
    public IdEncoder<String, Integer> idEncoder() {
        return HashIdEncoder.getInstance(getClass().getSimpleName());
    }
}
