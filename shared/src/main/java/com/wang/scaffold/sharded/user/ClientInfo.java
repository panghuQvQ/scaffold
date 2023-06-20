package com.wang.scaffold.sharded.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wang.scaffold.consts.UserDevice;
import lombok.Data;

@Data
public class ClientInfo {
    /**
     * 应用版本，使用浏览器时，为null
     */
    private String appVersion;
    /**
     * 操作系统，Windows,Mac OS,iOS,Android,Linux,Unknown
     */
    private String os;
    /**
     * 运行环境,Chrome,Safari,Firefox,Edge(Edg),Dart,Unknown
     */
    private String runtime;
    /**
     * ipv4地址
     */
    private String ipv4;
    /**
     * 用户设备(主要是用户登录的时候识别)
     */
    private UserDevice device;

    @JsonIgnore
    private String userAgent;

    @JsonIgnore
    public boolean isApp() {
        return runtime != null && runtime.startsWith("Dart/");
    }
}
