package com.huijiewei.agile.app.open.application.request;

import com.huijiewei.agile.app.open.application.service.DistrictExistsService;
import com.huijiewei.agile.core.constraint.Exists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author huijiewei
 */

@Data
public class DistrictRequest {
    @NotNull
    @Schema(description = "上级地区", required = true)
    @Exists(existService = DistrictExistsService.class, targetProperty = "id", allowValues = "0", message = "地区不存在")
    private Integer parentId;

    @NotBlank
    @Schema(description = "名称", required = true)
    private String name;

    @NotBlank
    @Schema(description = "代码", required = true)
    private String code;

    @NotNull
    @Schema(description = "邮编")
    private String zipCode;

    @NotNull
    @Schema(description = "区号")
    private String areaCode;
}
