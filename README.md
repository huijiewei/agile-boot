SpringBoot 2.3+ 脚手架项目

### 特点

- 基于最新版的 Spring Boot
- 六边形架构(Hexagonal Architecture)
- 基于 JDBC Template 的批量插入方案
- 树形数据使用闭包表(Closure Table)
- 待补充...

### 子项目列表
- agile-engine         核心代码
- agile-console        命令行应用
- agile-serve-admin    管理后台 API
- agile-spring-upload  上传功能模块
- agile-spring-captcha 验证码功能模块

### 开发环境配置方法
复制 config/application-example.yaml 到同目录，命名为 application-default.yaml

修改 application-default.yaml 里面的相关配置

### 数据库初始化
运行 agile-console(ConsoleApplication) 项目，选择初始化数据库

### 区域数据导入
下载省市县数据库 https://github.com/modood/Administrative-divisions-of-China/blob/master/dist/data.sqlite

重命名为 district.sqlite 并保存到 database 目录

运行 agile-console(ConsoleApplication) 项目，选择区域数据导入

### 运行
直接使用 IntelliJ IDEA 打开项目直接运行即可

#### 配合前端 Agile Vue 

https://github.com/huijiewei/agile-vue

管理员：13012345678 密码：123456