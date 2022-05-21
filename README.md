SpringBoot 2.3+ 脚手架项目

[![Intel](https://img.shields.io/badge/Intel-Core_i5_9600K-0071C5?style=flat&logo=intel&logoColor=white)](https://www.intel.com/content/www/us/en/products/sku/134896/intel-core-i59600k-processor-9m-cache-up-to-4-60-ghz/specifications.html)
[![Asrock](https://img.shields.io/badge/Z390M--ITX/ac-000.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAAAUCAMAAABMHminAAAAjVBMVEUAAACGwiaFwiaGwyeGwyaFwSaDwiaFwyaGwiaFwiWEwiaEwiaEwyeFwiaFwiaFwiaFwyaFwiaFwiaFwiaGwiWEwiaFwiWFwiaLyySFwiaGwiWDwCWFwiaFwiWFwiaFwiaGwiWGwiaDwx6GwiWFwyaFwiWFwiaFwiWFwiaMximFwyWEwyWHwCaFwiaFwiaMZ9zAAAAALnRSTlMAqowzIpkTqWVcn7VV+/iId+/nf0xDOywD80gN4dvXyaVUCNK/r5J6cwaDbRrDyeGVrgAAAe1JREFUOMvtk+lyozAMgBXABlPucBVycIYcrd//8VYyLpvOBDqz+7ffTGIP0vizkIBf/pOPe7Mjaij27k5jCr1xJwvWyDB+Vbui+8GRyBkeyr9UxrINHrCCg1GbNvk13ZbYUiPka4n0i58klsg3HSYmvu9P+O/iccLU1Cg5R1HkksXBvM6scg4Llpm13lVLOs/z+JbkjQ5R/4OUJ1gw0ErrESMpWE1AtnM792KMJWFoCcnaDUeO8cNNSRj+9h6SWqWS9GEYjhSvW1IkPpVbAqfkJ0npYDTYqmTAvMdcT4G5moOHEo3/ZlFkyJ1qT9coP+npmBacO/Sq+3cajq1CMrphN0u6NJYL3iIZa8DGxDmeNdm4UfNx1Y3X+BlscMaMSHfmBlxEhIHn9Sg5NTgPuNSYNVAVR0fNB+Uukvjo00WLdUdKWfUi+eKCT+fGCzrhMuILpNQGH/pkk9XTCHMbl89VR9nTSwYtsTRmdcQ5miWqNQkJDGHfqwBFPKFOPOpFArcDrqulCErnWvIdoSWmJP2F3pU7YFknDnmi2pD051CPMIXFWiH0DYTwSsJASzqJmCAOkggYXYlfYvntO5nUiL7G3DWMfajttHvCNnKAljF1uTt+LBZeKIuMu+Bf1zOdKWTMY4w5NKO4ZvDLP/IHwbpgg9Ud2r0AAAAASUVORK5CYII=&labelColor=333&label=Asrock&color=green)](https://www.asrock.com/mb/Intel/Z390M-ITXac/index.asp)
[![AMD](https://img.shields.io/badge/AMD-Radeon_RX_5500XT-ED1C24?style=flat&logo=amd&logoColor=white)](https://www.amd.com/en/products/graphics/amd-radeon-rx-5500-xt)
[![Dell](https://img.shields.io/badge/Dell-P2415Q-007DB8?style=flat&logo=dell&logoColor=white)](https://www.dell.com)

[![macOS](https://img.shields.io/badge/macOS-000000?style=flat&logo=apple&logoColor=white&color=2e118a)](https://www.apple.com/macos)
[![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=flat&logo=intellij-idea)](https://jb.gg/OpenSourceSupport)
[![Git](https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white)](https://git-scm.com/)
[![Sourcetree](https://img.shields.io/badge/Sourcetree-0052CC?style=flat&logo=Sourcetree&logoColor=white)](https://sourcetreeapp.com)

[![Gradle](https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white)](https://gradle.org/)

[![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=flat&logo=spring-boot)](https://spring.io/projects/spring-boot)

[![GitHub](https://img.shields.io/badge/GitHub-100000?style=flat&logo=github&logoColor=white)](https://github.com)
[![GitHub license](https://img.shields.io/github/license/huijiewei/agile-boot)](https://github.com/huijiewei/agile-boot)
[![GitHub issues](https://img.shields.io/github/issues/huijiewei/agile-boot)](https://GitHub.com/huijiewei/agile-boot/issues)


### 特点

- 基于最新版的 Spring Boot
- 六边形架构(Hexagonal Architecture)
- 基于 JDBC Template 的批量插入方案
- 树形数据使用闭包表(Closure Table)
- 待补充...

### 子项目列表

- agile-engine 核心代码
- agile-console 命令行应用
- agile-serve-admin 管理后台 API
- agile-spring-upload 上传功能模块
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