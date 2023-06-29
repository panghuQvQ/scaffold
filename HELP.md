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


### 使用 AJ-Captcha 实现滑动验证码
1. 引入依赖
2. 设置配置文件 application-captcha.yml
3. 引入图片
4. captchaService.verification(captchaVO) 方法，验证图片

### WebSocket 实现
1. 引入依赖 spring-boot-starter-websocket
2. 添加配置文件 WebSocketConfig
3. 引入依赖 spring-security-messaging
4. 添加WebSocket 安全配置 WebSocketSecurityConfig
