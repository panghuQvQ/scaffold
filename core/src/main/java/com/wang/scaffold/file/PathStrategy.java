package com.wang.scaffold.file;

/**
 * 存储路径策略
 * @author zhou wei
 *
 */
public interface PathStrategy {

	/** 资源存储路径 */
	public String getRepository();

	/** 储存地址前缀 */
	public String getLocationPrefix();

	/** 页面访问资源url地址前缀 */
	public String getUrlPrefix();

}
