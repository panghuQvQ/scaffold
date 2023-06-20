package com.wang.scaffold.file.support;

import com.wang.scaffold.file.UploadFileEntity;
import lombok.Data;

import java.util.List;

@Data
public class SimpleUploadFileEntity implements UploadFileEntity {

	private String fileEntityId;
	private List<String> fileUrls;


	public SimpleUploadFileEntity() {
	}

	public SimpleUploadFileEntity(UploadFileEntity entity) {
		this.fileEntityId = entity.getFileEntityId();
		this.fileUrls = entity.getFileUrls();
	}

	public SimpleUploadFileEntity(String fileEntityId, List<String> fileUrls) {
		this.fileEntityId = fileEntityId;
		this.fileUrls = fileUrls;
	}

	public void setFileEntityId(String fileEntityId) {
		this.fileEntityId = fileEntityId;
	}

	public void setFileUrls(List<String> fileUrls) {
		this.fileUrls = fileUrls;
	}

	@Override
	public String getFileEntityId() {
		return fileEntityId;
	}

	@Override
	public List<String> getFileUrls() {
		return fileUrls;
	}

}
