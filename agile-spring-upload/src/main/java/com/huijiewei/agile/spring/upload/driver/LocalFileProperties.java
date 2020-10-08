package com.huijiewei.agile.spring.upload.driver;

import com.huijiewei.agile.spring.upload.UploadProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    private String uploadPath = "./files";
    private String accessPath = "/files/**";
    private String corpAction;
    private String uploadAction;
    private String policyKey;
}
