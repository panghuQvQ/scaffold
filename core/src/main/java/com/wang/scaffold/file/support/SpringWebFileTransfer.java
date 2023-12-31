package com.wang.scaffold.file.support;

import com.wang.scaffold.file.*;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 *
 * Spring MVC在Controller中使用{@link MultipartFile}接收文件
 *
 * <p>设置{@link RenameStrategy}可以使上传的文件重命名（可选），
 * <p>设置{@link PathStrategy}可以使文件路径按规律变化，最简单的是一个固定路径比如D:/files/所有上传的文件都存入这个文件夹；
 * <p>也可以按照月份分比如D:/files/2020/05/，具体的实现方法是重写{@link PathStrategy#getRepository()}
 *
 * @author zhou wei
 *
 */
public class SpringWebFileTransfer implements FileTransfer {

	private MultipartFile multipartFile; // 多文件
	private TransferHandler handler;
	private RenameStrategy renameStrategy; // 重命名策略
	private PathStrategy pathStrategy; // 路径策略

	private String path; // 路径
	private String pathPrefix;
	private String urlPrefix;
	private String fileName;

	public SpringWebFileTransfer() {
	}

	/**
	 *
	 * @param handler
	 * @param renameStrategy
	 * @param pathStrategy
	 */
	public SpringWebFileTransfer(TransferHandler handler, RenameStrategy renameStrategy, PathStrategy pathStrategy) {
		this.handler = handler;
		this.renameStrategy = renameStrategy;
		this.pathStrategy = pathStrategy;
	}

	public SpringWebFileTransfer(MultipartFile multipartFile, TransferHandler handler, RenameStrategy renameStrategy,
			PathStrategy pathStrategy) {
		this.multipartFile = multipartFile;
		this.handler = handler;
		this.renameStrategy = renameStrategy;
		this.pathStrategy = pathStrategy;
	}

	/**
	 * 实例化方法
	 * @param multipartFile Spring web文件封装对象
	 * @return SpringWebFileTransfer对象
	 */
	public SpringWebFileTransfer buildFrom(MultipartFile multipartFile) {
		return new SpringWebFileTransfer(multipartFile, this.handler, this.renameStrategy, this.pathStrategy);
	}

	@Override
	public FileInfo transfer() throws IOException {
		Objects.requireNonNull(this.pathStrategy, "PathStrategy 不能为null");
		Objects.requireNonNull(multipartFile, "MultipartFile 不能为null");
		Objects.requireNonNull(handler, "FileHandler 不能为null");
		InputStream ins = multipartFile.getInputStream(); // 获取InputStream
		String originalFilename = multipartFile.getOriginalFilename(); // 获取文件的完整名称，文件名+后缀名
		if (this.path == null) {
			this.path = pathStrategy.getRepository(); // 设置 资源存储路径 /2023/10/
		}
		if (this.pathPrefix == null) {
			this.pathPrefix = pathStrategy.getLocationPrefix(); // 设置 储存地址前缀 D:/testFiles/
		}
		if (this.urlPrefix == null) {
			this.urlPrefix = pathStrategy.getUrlPrefix(); // 设置 页面访问资源url地址前缀 http://localhost:8086/files/public/
		}
		if (this.fileName == null) {
			if (this.renameStrategy != null) {
				this.fileName = this.renameStrategy.rename(originalFilename);
			} else {
				this.fileName = originalFilename;
			}
			String originalExtension = FilenameUtils.getExtension(originalFilename); // JDK提供方法：获取原始文件后缀
			String extension = FilenameUtils.getExtension(this.fileName); // 获取 根据重命名策略生成的文件名 的后缀
			if (!StringUtils.hasText(extension) && StringUtils.hasText(originalExtension)) {
				this.fileName = this.fileName + "." + originalExtension; // 拼接 重命名文件名 + 原始文件后缀
			}
		}
		return handler.doTransfer(ins, originalFilename, fileName, path, pathPrefix, urlPrefix);
	}

	/**
	 * 指定path，如果不指定，则由{@link PathStrategy#getRepository()}决定。
	 * @param path
	 * @return this
	 */
	public SpringWebFileTransfer path(String path) {
		this.path = path;
		return this;
	}

	/**
	 * 指定pathPrefix，如果不指定，则由{@link PathStrategy#getLocationPrefix()}决定。
	 * @param pathPrefix
	 * @return this
	 */
	public SpringWebFileTransfer pathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
		return this;
	}

	/**
	 * 指定urlPrefix，如果不指定，则由{@link PathStrategy#getUrlPrefix()}决定。
	 * @param urlPrefix
	 * @return this
	 */
	public SpringWebFileTransfer urlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
		return this;
	}

	/**
	 * 指定fileName，如果不指定，则由{@link RenameStrategy#rename(String)}决定。
	 * @param fileName
	 * @return this
	 */
	public SpringWebFileTransfer fileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public TransferHandler getHandler() {
		return handler;
	}

	public void setHandler(TransferHandler handler) {
		this.handler = handler;
	}

	public RenameStrategy getRenameStrategy() {
		return renameStrategy;
	}

	public void setRenameStrategy(RenameStrategy renameStrategy) {
		this.renameStrategy = renameStrategy;
	}

	public PathStrategy getPathStrategy() {
		return pathStrategy;
	}

	public void setPathStrategy(PathStrategy pathStrategy) {
		this.pathStrategy = pathStrategy;
	}
}
