package com.wang.scaffold.user.request;

import com.wang.scaffold.user.dto.vo.UserVO;
import lombok.Data;

/**
 * 更改用户状态请求参数
 *
 * @author gu ping
 *
 */
@Data
public class UpdateUserStatusRequest {
	private String _id;
	private Boolean status;

	public Integer getId() {
		UserVO temp = new UserVO();
		temp.set_id(_id);
		return temp.getId();
	}
}
