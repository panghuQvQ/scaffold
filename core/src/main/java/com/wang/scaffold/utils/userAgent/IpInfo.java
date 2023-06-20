package com.wang.scaffold.utils.userAgent;

import lombok.Data;

import java.io.Serializable;

@Data
public class IpInfo implements Serializable {
    private static final long serialVersionUID = -5171791015665080058L;
    private String ip;
    private String country;
    private String area;
    private String region;
    private String city;
    private String county;
    private String isp;
    private String country_id;
    private String area_id;
    private String region_id;
    private String city_id;
    private String county_id;
    private String isp_id;
}
