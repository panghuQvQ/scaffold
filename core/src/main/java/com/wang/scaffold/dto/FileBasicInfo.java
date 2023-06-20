package com.wang.scaffold.dto;

import lombok.Data;

@Data
public class FileBasicInfo {

	/**
	 * 原始文件名
	 */
	private String originalName;

	/**
	 * 文件路径
	 */
	private String uri;

}
