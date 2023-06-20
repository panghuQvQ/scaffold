package com.wang.scaffold.file;

/**
 *
 * 文件重命名策略
 * @author zhou wei
 *
 */
@FunctionalInterface
public interface RenameStrategy {

	public String rename(String original);

}
