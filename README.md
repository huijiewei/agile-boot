SpringBoot 2.3+ 脚手架项目

### 子项目列表
- agile-engine
- agile-serve-admin
- agile-spring-upload

### 配合前端 Agile Vue 

https://github.com/huijiewei/agile-vue

### 开发环境配置方法
复制 agile-serve-admin/resources/application-example.yaml 到同目录，命名为 application-default.yaml

修改 application-default.yaml 里面的相关配置

### 注意
数据表前缀需要修改两个地方
一个是 application-default.yaml 里面的配置
一个是数据初始化脚本 agile-serve-admin/src/main/database/migration/TableName 里面的配置