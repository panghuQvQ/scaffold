## 帮助文件

spring cloud微服务架构后端脚手架

项目组成：
* api 网关(spring cloud gateway)
* parent pom管理
* core 公共代码
* shared security, context等共享组件
* eureka 服务组件注册服务器
* files 文件服务
* user 用户

### maven commands
mvn clean package -P docker -DskipTests

### 移动打包好后的文件
修改、运行 gatherWarPackages.bat

### docker compose
##### 全部启动
docker compose up // -d --no-start --no-recreate --no-build
##### build
docker compose build [service1, service2...]
##### start
docker compose start [service1, service2...]
##### restart
docker compose restart [service1, service2...]


### AJ-Captcha 实现滑动验证码大纲
1. 引入依赖
2. 设置配置文件 application-captcha.yml
3. 引入图片
4. captchaService.verification(captchaVO) 方法，验证图片

### Stomp WebSocket 实现大纲
1. 引入依赖 spring-boot-starter-websocket
2. 添加配置文件 WebSocketConfig.class
3. 引入依赖 spring-security-messaging 集成 Spring Security
4. 添加WebSocket 安全配置 WebSocketSecurityConfig.class
5. 封装工具类,用于发送消息广播或推送, NotifyController.class


### Spring Cloud Bus AMQP 实现消息的订阅与广播大纲
1. docker 配置 rabbimq
2. 引入依赖 spring-cloud-starter-bus-amqp
3. 实现自定义事件 RoleModifyEvent.class
4. 实现自定义发布器 RoleModifyEventPublisher.class
5. 实现自定义事件订阅者 RolePermissionCacheDao.class
6. 在所需模块添加 自定义事件配置 CloudEventListeners.class

### 自定义 yaml 文件提示信息,实现大纲
1. 引入依赖 spring-boot-configuration-processor
2. 编写配置类,例如 JwtProperties.class 使用 @ConfigurationProperties(prefix="xxx")
   1. 配置类: 注意属性私有,且需要生成get、set方法
   2. @EnableConfigurationProperties(JwtProperties.class) 或 @Component 引入Spring容器
3. yaml 提示编写, 在 resources---> META-INF ---> additional-spring-configuration-metadata.json 中添加
   - name: 属性全名,例：jwt
   - type: 属性的数据类型的完整签名,例：com.wang.scaffold.sharded.security.JwtProperties
   - description: 属性描述,该属性可在类中添加注释即可
   - defaultValue: 默认值,该属性可在类中添加默认值

