package com.wang.scaffold.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class FileMoveDTO {

	/**
	 * 旧地址
	 */
	private List<FileBasicInfo> src;

	/**
	 * 保护路径的path
	 */
	@NotEmpty
	private String path;

}
