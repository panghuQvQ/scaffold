package com.wang.scaffold.files.image;

import com.wang.scaffold.files.imgscalr.Scalr;
import com.wang.scaffold.files.imgscalr.Scalr.Method;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

@Component
public class CropAndResize {

	@Async
	public CompletableFuture<BufferedImage> resizeAsync(BufferedImage src, int width, int height) {
		BufferedImage result = Scalr.resize(src, Method.ULTRA_QUALITY, width, height);
		return CompletableFuture.completedFuture(result);
	}

	public BufferedImage resize(BufferedImage src, int width, int height) {
		return Scalr.resize(src, Method.ULTRA_QUALITY, width, height);
	}
}
