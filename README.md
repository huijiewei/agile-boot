SpringBoot 2.3+ 脚手架项目

### 子项目列表
- agile-engine         核心代码
- agile-console        命令行应用
- agile-serve-admin    管理后台 API
- agile-spring-upload  上传功能模块
- agile-spring-captcha 验证码功能模块

### 开发环境配置方法
复制 config/application-example.yaml 到同目录，命名为 application-default.yaml

修改 application-default.yaml 里面的相关配置

### 区域数据导入
下载省市县数据库 https://github.com/modood/Administrative-divisions-of-China/blob/master/dist/data.sqlite

重命名为 district.sqlite 并保存到 database 目录

运行 agile-console(ConsoleApplication) 项目，选择导入选项

### 运行

#### 配合前端 Agile Vue 

https://github.com/huijiewei/agile-vue

管理员：13012345678 密码：123456