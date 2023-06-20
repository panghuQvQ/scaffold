package com.wang.scaffold.file.support;


import com.wang.scaffold.file.RenameStrategy;
import com.wang.scaffold.utils.friendlyId.FriendlyId;

/**
 * 使用UUID重命名文件
 * @author zhou wei
 *
 */
public class RenameByFriendlyId implements RenameStrategy {

	@Override
	public String rename(String original) {
		return FriendlyId.createFriendlyId();
	}

}
