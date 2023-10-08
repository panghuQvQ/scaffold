package com.wang.scaffold.files.controller;

import com.wang.scaffold.file.FileInfo;
import com.wang.scaffold.file.support.SpringWebFileTransfer;
import com.wang.scaffold.files.config.ProtectedFileInterceptor;
import com.wang.scaffold.files.config.UploadFileProperties;
import com.wang.scaffold.files.consts.RedisKeyConsts;
import com.wang.scaffold.files.image.CropAndResize;
import com.wang.scaffold.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.wang.scaffold.files.config.UploadFileProperties.LocationConfig;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

/**
 * 图片上传
 */
@RestController
public class FileUploadController {

	@Autowired
	private SpringWebFileTransfer diskFileTransfer; // 保存磁盘,并重命名文件
	@Autowired
	private SpringWebFileTransfer diskProtectedFileTransfer; // 保存磁盘,不重命名文件
	@Autowired
	private CropAndResize cropAndResize;
	@Autowired
	private UploadFileProperties uploadFileProperties;
	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 普通上传，文件可以自由访问，文件存储会按年/月来生成路径存储，文件会被重命名为随机文件名。
	 */
	@PostMapping(path = "/upload")
	public BaseResponse<FileInfo> upload(MultipartFile file, boolean dangling) throws IOException {
		FileInfo info = diskFileTransfer.buildFrom(file).transfer();
		if (dangling) {
			this.setDangling(info.getUri());
		}
		return BaseResponse.success("上传成功", info);
	}

	/**
	 * 安全上传，文件会放置到安全文件路径。放置在安全路径的文件，文件不会被重命名，同名文件将被覆盖。读取时需要token
	 * @see {@link ProtectedFileInterceptor}
	 * @param path 文件存储路径，调用方定义
	 */
	@PostMapping(path = "/safe-upload")
	public BaseResponse<FileInfo> safeUpload(MultipartFile file, String path, boolean dangling) throws IOException {
		FileInfo info = diskProtectedFileTransfer.buildFrom(file).path(path).transfer();
		if (dangling) {
			this.setDangling(info.getUri());
		}
		return BaseResponse.success("上传成功", info);
	}

	/**
	 * 安全删除文件。操作删除文件时，生成一个uuid作为key，文件url路径作为value储存在redis里面。
	 * <br>本api执行删除文件时，先通过key从redis里面读取需要删除的文件的url，然后找到对应的磁盘地址，执行文件删除操作。
	 * @param key redis文件url路径值对应的key
	 */
	@RequestMapping(path = "/safe-delete")
	public BaseResponse<String> safeDelete(String key) {
		String path = redisTemplate.opsForValue().get(key);
		if (path == null) return BaseResponse.fail("未找到文件[redis]");

		String diskPath = null; // 存储路径
		Map<String, LocationConfig> savedLocations = uploadFileProperties.getLocations();
		for (Entry<String, LocationConfig> entry : savedLocations.entrySet()) {
			LocationConfig locationConfig = entry.getValue();
			String urlPrefix = locationConfig.getUrlMapping();
			if(path.startsWith(urlPrefix)) {
				diskPath = path.replaceFirst(urlPrefix, locationConfig.getStoragePath());
				break;
			}
		}
		if (diskPath == null) return BaseResponse.fail("未找到文件[disk]");

		try {
			Path p = Paths.get(diskPath);
			Files.delete(p);
		} catch (IOException e) {
			e.printStackTrace();
			return BaseResponse.fail("删除失败");
		} finally {
			redisTemplate.delete(key);
		}
		return BaseResponse.success();
	}
	/**
	 * @param dangling {@link #setDangling}
	 * @param w        传入w=?或者h=?则制作一张按宽高要求的缩略图
	 */
	@PostMapping(path = "/upload-img")
	public BaseResponse<FileInfo> uploadImg(MultipartFile file, boolean dangling,
											@RequestParam(required = false) Integer w, @RequestParam(required = false) Integer h) throws IOException {
		FileInfo info = diskFileTransfer.buildFrom(file).transfer();

		int width = 0;
		int height = 0;
		if (w != null) {
			width = w;
			if(h == null) height = w;
		}
		if (h != null) {
			height = h;
			if(w == null) width = h;
		}
		if (width > 0 && height > 0) {
			BufferedImage bufImg = ImageIO.read(file.getInputStream());
			CompletableFuture<BufferedImage> future = cropAndResize.resizeAsync(bufImg, width, height);

			future.whenComplete((result, exception) -> {
				boolean done = false;
				if (exception == null) {
					File f = new File(getThumbnailUri(info));
					Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(info.getExtension());
					if (writers.hasNext()) {
						ImageWriter writer = writers.next();
						try {
							try (ImageOutputStream output = ImageIO.createImageOutputStream(f)) {
								writer.setOutput(output);
								writer.write(result);
								done = true;
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} finally {
							// Dispose writer in finally block to avoid memory leaks
							writer.dispose();
						}
					}
				}
				if (exception != null || !done) {
					makeSelfCopy(info);
				}
			});
		}
		if (dangling) {
			this.setDangling(info.getUri());
		}
		return BaseResponse.success("上传成功", info);
	}

	private void makeSelfCopy(FileInfo fileInfo) {
		try {
			Files.copy(Paths.get(URI.create(fileInfo.getUri())),
					Paths.get(URI.create(getThumbnailUri(fileInfo))),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getThumbnailUri(FileInfo fileInfo) {
		String uri = fileInfo.getInternalUri();
		return uri.substring(0, uri.lastIndexOf(".")) + "-thumbnail" + uri.substring(uri.lastIndexOf("."));
	}

	/**
	 * 设置文件为悬空状态。悬空状态的意思就是没有哪条数据库的数据确认这个文件的归属。
	 * @param uri
	 */
	private void setDangling(String uri) {
		BoundZSetOperations<String, String> ops = redisTemplate.boundZSetOps(RedisKeyConsts.zset_dangling);
		ops.add(uri, System.currentTimeMillis());
	}
}
