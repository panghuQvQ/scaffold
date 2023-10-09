package com.wang.scaffold.files.config.filePath;


import com.wang.scaffold.file.PathStrategy;
import com.wang.scaffold.files.config.UploadFileProperties;

import java.util.Objects;

/**
 * @title 属性配置路径策略
 * @description 
 * @author wzy 
 * @updateTime 2023/9/26 16:12 
 * @throws 
 */
public class PropertiesConfigPathStrategy implements PathStrategy {

	private UploadFileProperties.LocationConfig locationConfig;

	public PropertiesConfigPathStrategy(UploadFileProperties.LocationConfig locationConfig) {
		Objects.requireNonNull(locationConfig, "路径配置不能为空");
		this.locationConfig = locationConfig;
	}

	@Override
	public String getLocationPrefix() {
		return locationConfig.getStoragePath();
	}

	@Override
	public String getUrlPrefix() {
		return locationConfig.getUrlMapping();
	}

	@Override
	public String getRepository() {
		return "";
	}
}
