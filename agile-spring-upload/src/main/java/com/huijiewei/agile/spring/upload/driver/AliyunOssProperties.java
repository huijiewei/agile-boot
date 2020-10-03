package com.huijiewei.agile.spring.upload.driver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author huijiewei
 */

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = AliyunOssProperties.PREFIX)
public class AliyunOssProperties {
    public static final String PREFIX = "agile.spring.upload.aliyun-oss";

    private String accessKeyId;
    private String accessKeySecret;

    private String endpoint;
    private String bucket;
    private String directory = "";
    private String styleDelimiter = "";
}
