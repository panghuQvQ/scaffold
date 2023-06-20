package com.wang.scaffold.file;

import java.io.IOException;

/**
 *
 * 文件转移主要接口
 * @author zhou wei
 *
 */
public interface FileTransfer {

	public FileInfo transfer() throws IOException;

}
