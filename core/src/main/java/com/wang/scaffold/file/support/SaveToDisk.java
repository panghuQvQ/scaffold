package com.wang.scaffold.file.support;

import com.wang.scaffold.file.FileInfo;
import com.wang.scaffold.file.TransferHandler;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 保存到磁盘。
 *
 */
public class SaveToDisk implements TransferHandler {

	@Override
	public FileInfo doTransfer(InputStream ins, String originalFileName, String fileName, String path,
							   String pathPrefix, String urlPrefix) throws IOException {
		Path dir = Paths.get(pathPrefix, path).normalize(); // 拼接路径,并删掉多余 ...
		Files.createDirectories(dir); // 如果被创建文件夹的父文件夹不存在，就创建它，如果被创建的文件夹已经存在，就是用已经存在的文件夹，不会重复创建，没有异常抛出
		Path filePath = Paths.get(dir.toString(), fileName);
		OutputStream out = Files.newOutputStream(filePath);
		int byteCount = 0;
		byte[] buffer = new byte[8192];
		int bytesRead = -1;
		while ((bytesRead = ins.read(buffer)) != -1) { // 读取若干字节并填充到byte[]数组，返回读取的字节数
			out.write(buffer, 0, bytesRead);
			byteCount += bytesRead;
		}
		out.flush();
		out.close();
		FileInfo info = new FileInfo(); // FileInfo类是一个密封类，它可以用来创建、复制、删除、移动和打开文件的实例方法
		info.setOriginalName(originalFileName);
		info.setFileName(fileName);
		info.setExtension(FilenameUtils.getExtension(originalFileName));
		info.setSize(byteCount);
		info.setInternalUri(filePath.toString());
		URI uri = URI.create(urlPrefix + "/" +  path + "/" + fileName).normalize();
		info.setUri(uri.toString());
		return info;
	}

//	public static void main(String[] args) throws IOException {
//		SaveToDisk obj = new SaveToDisk();
//		FileInputStream fins = new FileInputStream("/Users/weizhou/devfiles/byx_erp.sql");
//		FileInfo info = obj.doTransfer(fins, "byx_erp.sql", "temp.sql", "/test/", "/Users/weizhou/devfiles/tmp/", "/public");
//		System.out.println(info);
//		String urlPrefix = "http://localhost:8086/files/protected/";
//		String path = "/document/BYX-D-2019-085-012/";
//		String fileName = "商城大街 开票合同补充协议（银江-李明电子）-2.pdf";
//		Path p = Paths.get("/", path, "/", fileName).normalize();
//		String part2 = p.toString();
//		if (urlPrefix.endsWith("/")) {
//			urlPrefix = urlPrefix.substring(0, urlPrefix.length() - 1);
//		}
//		System.out.println(urlPrefix + part2);
//	}
}
