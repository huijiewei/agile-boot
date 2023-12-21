package com.huijiewei.agile.core.config;

import io.swagger.v3.oas.models.media.IntegerSchema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;

@Lazy(false)
@Configuration(proxyBeanMethods = false)
public class OpenApiBaseConfig {
    @Value("${spring.data.web.pageable.size-parameter}")
    private String pageableSizeParameter = "size";

    @Value("${spring.data.web.pageable.page-parameter}")
    private String pageablePageParameter = "page";

    @Value("${spring.data.web.pageable.one-indexed-parameters}")
    private Boolean pageableOneIndexedParameter = false;

    @Value("${spring.data.web.pageable.default-page-size}")
    private Integer pageableDefaultPageSizeParameter = 20;

    @Bean
    public OpenApiCustomizer openApiPageableCustomizer() {
        return openApi -> openApi.getComponents().getSchemas().values().forEach(schema -> {
            if (schema.getName() != null && schema.getName().equals("Pageable")) {
                schema.getProperties().clear();
                schema
                        .addProperty(pageablePageParameter, new IntegerSchema()
                                .description("页码")
                                .minimum(BigDecimal.valueOf(pageableOneIndexedParameter ? 1 : 0))
                        )
                        .addProperty(pageableSizeParameter, new IntegerSchema().description("分页大小")
                                .minimum(BigDecimal.valueOf(1))
                        );
            }
        });
    }

}
