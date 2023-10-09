package com.wang.scaffold.files.config;

import com.wang.scaffold.file.PathStrategy;
import com.wang.scaffold.file.RenameStrategy;
import com.wang.scaffold.file.TransferHandler;
import com.wang.scaffold.file.support.RenameByFriendlyId;
import com.wang.scaffold.file.support.SaveToDisk;
import com.wang.scaffold.file.support.SpringWebFileTransfer;
import com.wang.scaffold.files.config.filePath.PropertiesConfigPathStrategy;
import com.wang.scaffold.files.config.filePath.PublicDiskFilePathStrategy;
import com.wang.scaffold.files.utils.FilenameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName WebConfig.java
 * @Description 可以添加自定义的拦截器(跨域设置、类型转换器等等)、消息转换器 等等
 * @createTime 2023年07月01日 09:45:00
 */
@EnableConfigurationProperties(UploadFileProperties.class)
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UploadFileProperties uploadFileProperties;

    @Autowired
    ProtectedFileInterceptor protectedFileInterceptor;


    /**
     * 添加静态资源---自定义资源映射。这个东西也比较常用，业务场景就是自己的服务器作为文件服务器，不利用第三方的图床，就需要一个虚拟路径映射到我们服务器的地址。
     * 值得一提的是，如果你的项目是war包启动，一般都是在 Tomcat 中配置一下（配置方法请百度）；
     * 如果是jar包启动（SpringBoot经常用这种方式启动），就可以用到这个方法了。
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // .addResourceLocations("files:XXX") 可以指定磁盘绝对路径，同样可以配置多个位置，注意路径写法需要加上file:
        // .addResourceLocations("classpath:/static/")
        // 访问public 文件夹中的fengjing.jpg 图片的地址为 http://localhost:8080/public/fengjing.jpg  会路由到本地磁盘(D:/testFiles/fengjing.jpg)路径下寻找
        registry.addResourceHandler("/public/**").addResourceLocations("file:"+uploadFileProperties.getPublicDiskLocationConfig().getStoragePath());
        registry.addResourceHandler("/protected/**").addResourceLocations("file:" + uploadFileProperties.getProtectedDiskLocationConfig().getStoragePath());
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 自定义写拦截器，并指定拦截路径
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 如果是请求 "/protected/**" , 则需要经过拦截器校验
        registry.addInterceptor(protectedFileInterceptor).addPathPatterns("/protected/**");
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                StringBuffer uri = request.getRequestURL();
                String filename = uri.substring(uri.lastIndexOf("/") + 1);
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                return true;
            }
        }).addPathPatterns("/public/mobile_app/*.apk");
    }

    /**
     * 跨域支持
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/public/**");
        registry.addMapping("/protected/**");
    }

    /**
     * 保存在磁盘，repository由{@link PublicDiskFilePathStrategy}定义，使用FriendlyId重命名文件。
     */
    @Bean
    public SpringWebFileTransfer diskFileTransfer() {
        TransferHandler transferHandler = new SaveToDisk(); // 用于保存到磁盘的对象
        RenameStrategy renameStrategy = new RenameByFriendlyId(); // 使用UUID重命名文件
        PathStrategy pathStrategy = new PublicDiskFilePathStrategy(uploadFileProperties.getPublicDiskLocationConfig()); // 定义 公共磁盘文件路径策略
        SpringWebFileTransfer fileTransfer = new SpringWebFileTransfer(transferHandler, renameStrategy, pathStrategy);
        return fileTransfer;
    }

    /**
     * 保存在磁盘，不重命名
     * unsafe字符需要被处理
     */
    @Bean
    public SpringWebFileTransfer diskProtectedFileTransfer() {
        TransferHandler transferHandler = new SaveToDisk();
        PathStrategy pathStrategy = new PropertiesConfigPathStrategy(uploadFileProperties.getProtectedDiskLocationConfig());
        RenameStrategy renameStrategy = (String original) -> {  // 使用Lambda表达式来表示该接口的一个实现
            return FilenameUtil.sanitizeName(original);
        };
        SpringWebFileTransfer fileTransfer = new SpringWebFileTransfer(transferHandler, renameStrategy, pathStrategy);
        return fileTransfer;
    }
}
