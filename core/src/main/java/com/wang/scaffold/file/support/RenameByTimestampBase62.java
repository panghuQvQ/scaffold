package com.wang.scaffold.file.support;


import com.wang.scaffold.file.RenameStrategy;
import com.wang.scaffold.utils.friendlyId.Base62;

public class RenameByTimestampBase62 implements RenameStrategy {

	@Override
	public String rename(String original) {
		long time = System.currentTimeMillis();
		return Base62.encode(time);
	}

}
