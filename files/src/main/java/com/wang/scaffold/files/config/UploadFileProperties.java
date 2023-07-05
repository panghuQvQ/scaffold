package com.wang.scaffold.files.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "file")
public class UploadFileProperties {

	public static final String PUBLIC_DISK_KEY = "disk";
	public static final String PROTECTED_DISK_KEY = "disk-protected";

	private Map<String, LocationConfig> locations;

	public Map<String, LocationConfig> getLocations() {
		return locations;
	}

	public void setLocations(Map<String, LocationConfig> locations) {
		this.locations = locations;
	}

	public LocationConfig getLocationConfig(String key) {
		if (this.locations == null) {
			throw new NullPointerException("未找到任何存储路径配置");
		}
		LocationConfig locationConfig = this.locations.get(key);
		if(locationConfig == null) {
			throw new NullPointerException("未配置" + key + "文件存储的磁盘路径");
		}
		return locationConfig;
	}

	public LocationConfig getPublicDiskLocationConfig() {
		return this.getLocationConfig(PUBLIC_DISK_KEY);
	}

	public LocationConfig getProtectedDiskLocationConfig() {
		return this.getLocationConfig(PROTECTED_DISK_KEY);
	}

	public static class LocationConfig {
		/** 上传地址(后端读取文件用地址) */
		private String storagePath;
		/** url路径映射(前端访问用地址) */
		private String urlMapping;

		public String getStoragePath() {
			return storagePath;
		}
		public void setStoragePath(String storagePath) {
			this.storagePath = storagePath;
		}
		public String getUrlMapping() {
			return urlMapping;
		}
		public void setUrlMapping(String urlMapping) {
			this.urlMapping = urlMapping;
		}
	}
}
