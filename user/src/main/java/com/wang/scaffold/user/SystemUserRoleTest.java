package com.wang.scaffold.user;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wang.scaffold.user.entity.Permission;
import com.wang.scaffold.user.entity.Role;
import com.wang.scaffold.user.entity.User;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 编译时，获取默认的用户与权限信息
 */
@Slf4j
public class SystemUserRoleTest {

    private static final List<ImmutableUser> defaultUsers;
    private static final List<ImmutableRole> defaultRoles;
    private static final List<ImmutableUser> systemUsers;
    private static final List<ImmutableRole> systemRoles;

    /**
     * 1、Java静态代码块中的代码会在类加载JVM时运行，且只被执行一次
     * 2、静态块常用来执行类属性的初始化
     * 3、静态块优先于各种代码块以及构造函数，如果一个类中有多个静态代码块，会按照书写顺序依次执行
     * 4、静态代码块可以定义在类的任何地方中除了方法体中【这里的方法体是任何方法体】
     * 5、静态代码块不能访问普通变量
     */
    static {
        // 反序列化的时候如果多了其他属性,不抛出异常
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {

            // Class.getResourceAsStream(String path) ： path 不以’/'开头时默认是从此类所在的包下取资源，以’/'开头则是从ClassPath根下获取。
            JsonNode rootNode = mapper.readTree(SystemUserRoleTest.class.getResourceAsStream("/default-users.json"));

            final List<User> dUsers = new ArrayList<>();
            final List<Role> dRoles = new ArrayList<>();
            final List<User> sUsers = new ArrayList<>();
            final List<Role> sRoles = new ArrayList<>();

            List<String> sus = new ArrayList<>();// 系统用户
            List<String> srs = new ArrayList<>();// 系统角色
            rootNode.path("systemUsers").forEach(u -> sus.add(u.asText()));
            rootNode.path("systemRoles").forEach(r -> srs.add(r.asText()));

            JsonNode rolesNode = rootNode.path("roles");
            if (!rolesNode.isMissingNode()) {
                for (Iterator<String> it = rolesNode.fieldNames(); it.hasNext(); ) {
                    String roleField = it.next();
                    JsonNode roleNode = rolesNode.get(roleField);
                    Role role = mapper.treeToValue(roleNode, Role.class);
                    dRoles.add(role);
                    if (srs.contains(roleField)) {
                        sRoles.add(role);
                    }
                }
            } else {
                log.warn("没有找到默认角色，请确认default-users.json是否配置正确");
            }

            JsonNode usersNode = rootNode.path("users");
            if (!usersNode.isMissingNode()) {
                for (Iterator<String> it = usersNode.fieldNames(); it.hasNext(); ) {
                    String userField = it.next();
                    JsonNode userNode = usersNode.get(userField);
                    User user = mapper.treeToValue(userNode, User.class);
                    List<String> roleNames = new ArrayList<>();
                    userNode.path("roleNames").forEach(rn -> roleNames.add(rn.asText()));
                    List<Role> userRoles = dRoles.stream().filter(role -> roleNames.contains(role.getName())).collect(Collectors.toList());
                    user.setRoles(userRoles);
                    dUsers.add(user);
                    if (sus.contains(userField)) {
                        sUsers.add(user);
                    }
                }
            } else {
                log.warn("没有找到默认用户，请确认default-users.json是否配置正确");
            }
            defaultUsers = dUsers.stream().map(ImmutableUser::new).collect(Collectors.toList());
            defaultRoles = dRoles.stream().map(ImmutableRole::new).collect(Collectors.toList());
            systemUsers = sUsers.stream().map(ImmutableUser::new).collect(Collectors.toList());
            systemRoles = sRoles.stream().map(ImmutableRole::new).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<ImmutableRole> getDefaultRoles() {
        return Collections.unmodifiableList(defaultRoles);
    }

    public static List<ImmutableUser> getDefaultUsers() {
        return Collections.unmodifiableList(defaultUsers);
    }

    public static boolean isSystemUser(User user) {
        String username = user.getUsername();
        return isSystemUser(username);
    }

    public static boolean isSystemUser(String username) {
        return systemUsers.stream().anyMatch(u -> u.getUsername().equals(username));
    }

    public static boolean isSystemRole(Role role) {
        String roleName = role.getName();
        return isSystemRole(roleName);
    }

    public static boolean isSystemRole(String roleName) {
        return systemRoles.stream().anyMatch(r -> r.getName().equals(roleName));
    }

    @Getter @ToString
    public static final class ImmutablePermission {
        private final String name;

        public ImmutablePermission(Permission permission) {
            this.name = permission.getName();
        }
    }

    @Getter @ToString
    public static final class ImmutableRole {

        private final String name;

        private final List<ImmutablePermission> permissions;

        public ImmutableRole(Role role) {
            this.name = role.getName();
            this.permissions = role.getPermissions() == null ?
                    new ArrayList<>() :
                    role.getPermissions().stream().map(ImmutablePermission::new).collect(Collectors.toList());
        }
    }

    @Getter @ToString
    public static final class ImmutableUser {
        private final String username;

        private final String name;
        /**
         * 头像
         **/
        private final String avatar;
        /**
         * 手机号码
         */
        private final String phone;
        /**
         * 邮箱
         */
        private final String email;

        private final String password;
        /**
         * 账号禁用
         */
        private final boolean disabled;

        private final List<ImmutableRole> roles;

        public ImmutableUser(User user) {
            this.username = user.getUsername();
            this.name = user.getName();
            this.avatar = user.getAvatar();
            this.phone = user.getPhone();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.disabled = user.isDisabled();
            this.roles = user.getRoles() == null ?
                    new ArrayList<>() :
                    user.getRoles().stream().map(ImmutableRole::new).collect(Collectors.toList());
        }
    }

//    public static void main(String[] args) {
//        System.out.println("---默认用户---");
//        defaultUsers.forEach(System.out::println);
//        System.out.println("---默认角色---");
//        defaultRoles.forEach(System.out::println);
//        System.out.println("---系统用户---");
//        systemUsers.forEach(System.out::println);
//        System.out.println("---系统角色---");
//        systemRoles.forEach(System.out::println);
//    }
}
