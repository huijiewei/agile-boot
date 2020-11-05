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
@ConfigurationProperties(prefix = LocalFileProperties.PREFIX)
public class LocalFileProperties {
    public static final String PREFIX = "agile.spring.upload.local-file";

    private String accessPath = "/files/**";
    private String uploadPath = "./files";
    private String corpAction;
    private String uploadAction;
    private String policyKey;
}
