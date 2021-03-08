package com.huijiewei.agile.spring.upload.driver;

import com.huijiewei.agile.spring.upload.util.UploadUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huijiewei
 */

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = LocalFileProperties.PREFIX, name = {"access-path", "upload-path"})
public class LocalFileConfig implements WebMvcConfigurer {
    private final LocalFileProperties properties;

    public LocalFileConfig(LocalFileProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        var accessPath = this.properties.getAccessPath();

        if (UploadUtils.isUrl(accessPath)) {
            return;
        }

        var absoluteUploadPath = UploadUtils.buildAbsoluteUploadPath(this.properties.getUploadPath());

        registry.addResourceHandler(accessPath)
                .addResourceLocations("file:" + absoluteUploadPath)
                .resourceChain(false);
    }
}
