package com.wang.scaffold.file;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author zhou wei
 *
 */
public interface TransferHandler {

	/**
	 * 执行文件Transfer，可能是写入本地磁盘，ftp上传，发给fastDSF文件服务器等。
	 * @param ins 文件流
	 * @param originalFileName 原始文件名
	 * @param fileName 存储文件名
	 * @param path 路径(和pathPrefix组合可以给后端访问到文件，和urlPrefix组合可以给前端页面访问到文件)
	 * @param pathPrefix 存储路径前缀
	 * @param urlPrefix url路径前缀
	 * @return {@link FileInfo}
	 * @throws IOException
	 */
	public FileInfo doTransfer(InputStream ins, String originalFileName, String fileName, String path, String pathPrefix, String urlPrefix) throws IOException;

}
