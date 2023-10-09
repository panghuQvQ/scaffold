package com.wang.scaffold.files.config.filePath;

import com.wang.scaffold.files.config.UploadFileProperties;

import java.util.Calendar;

/**
 * @title 公共磁盘文件路径策略
 * @description 
 * @author wzy 
 * @updateTime 2023/9/26 16:15 
 * @throws 
 */
public class PublicDiskFilePathStrategy extends PropertiesConfigPathStrategy {

	public PublicDiskFilePathStrategy(UploadFileProperties.LocationConfig locationConfig) {
		super(locationConfig);
	}

	@Override
	public String getRepository() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		String path = "/" + year + "/" + month + "/";
		return path;
	}

}
