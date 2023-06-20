package com.wang.scaffold.file;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 文件信息描述
 * @author zhou wei
 *
 */
public class FileInfo {

	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 原始文件名
	 */
	private String originalName;

	/**
	 * 文件路径
	 */
	private String uri;

	/**
	 * 内部地址
	 */
	@JsonIgnore
	private String internalUri;

	/**
	 * 文件后缀
	 */
	private String extension;

	/**
	 * 文件大小
	 */
	private long size;


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

//	@Deprecated @JsonSerialize(using = FileFieldSerializer.class)
	public String getUri() {
		return uri.toString();
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getInternalUri() {
		return internalUri;
	}

	public void setInternalUri(String internalUri) {
		this.internalUri = internalUri;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
}
