package com.wang.scaffold.file.support;

import com.wang.scaffold.file.RenameStrategy;

import java.util.UUID;

/**
 * 使用UUID重命名文件
 * @author zhou wei
 *
 */
public class RenameByUUID implements RenameStrategy {

	@Override
	public String rename(String original) {
		return UUID.randomUUID().toString();
	}

}
