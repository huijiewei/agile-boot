package com.huijiewei.agile.core.consts;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(in = ParameterIn.QUERY, name = "page", schema = @Schema(type = "integer", defaultValue = "0"))
@Parameter(in = ParameterIn.QUERY, name = "size", schema = @Schema(type = "integer", defaultValue = "20"))
public @interface ApiPageable {
    @Value("${spring.data.web.pageable.size-parameter}")
    String pageableSizeParameter = "size";

    @Value("${spring.data.web.pageable.page-parameter}")
    String pageablePageParameter = "page";

    @Value("${spring.data.web.pageable.one-indexed-parameters}")
    Boolean pageableOneIndexedParameter = false;

    @Value("${spring.data.web.pageable.default-page-size}")
    Integer pageableDefaultPageSizeParameter = 20;
}
