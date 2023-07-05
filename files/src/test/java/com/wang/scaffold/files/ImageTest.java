package com.wang.scaffold.files;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.util.Arrays;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName PublishTest.java
 * @Description TODO
 * @createTime 2023年06月26日 14:49:00
 */
@SpringBootTest
public class ImageTest {

    /**
     * 默认Java 11 不支持 .webp 格式的图片读取，所以需添加 imageio-webp 依赖
     * 以下代码，可输出默认的 Java 11 支持的图片格式(注意提前注释掉依赖)
     */
    @Test
    public void test(){
        String readFormats[] = ImageIO.getReaderFormatNames();
        String writeFormats[] = ImageIO.getWriterFormatNames();
        System.out.println("支持的Readers: " + Arrays.asList(readFormats));
        System.out.println("支持的Writers: " + Arrays.asList(writeFormats));
    }
}
