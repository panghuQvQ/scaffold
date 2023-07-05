package com.wang.scaffold.files.config.filePath;

import com.wang.scaffold.files.config.UploadFileProperties;

import java.util.Calendar;

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
