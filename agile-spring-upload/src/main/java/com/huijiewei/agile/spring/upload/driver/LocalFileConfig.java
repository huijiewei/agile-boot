package com.huijiewei.agile.spring.upload.driver;

import com.huijiewei.agile.spring.upload.util.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huijiewei
 */

@Configuration
@ConditionalOnProperty(prefix = LocalFileProperties.PREFIX, name = {"access-path", "upload-path"})
public class LocalFileConfig implements WebMvcConfigurer {
    private final LocalFileProperties properties;

    @Autowired
    public LocalFileConfig(LocalFileProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absoluteUploadPath = UploadUtils.buildAbsoluteUploadPath(this.properties.getUploadPath());

        registry.addResourceHandler(this.properties.getAccessPath())
                .addResourceLocations("file:" + absoluteUploadPath)
                .resourceChain(false);
    }
}
