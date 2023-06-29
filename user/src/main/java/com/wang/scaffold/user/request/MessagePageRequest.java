package com.wang.scaffold.user.request;


import com.wang.scaffold.request.BasePageRequest;

public class MessagePageRequest extends BasePageRequest {

	/** 已读状态 */
	private Boolean status;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
