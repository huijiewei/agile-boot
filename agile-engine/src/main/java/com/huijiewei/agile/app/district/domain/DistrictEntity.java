package com.huijiewei.agile.app.district.domain;

import com.huijiewei.agile.app.district.application.service.DistrictUniqueService;
import com.huijiewei.agile.core.constraint.Unique;
import com.huijiewei.agile.core.domain.AbstractTreeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Unique(fields = "code", uniqueService = DistrictUniqueService.class, message = "地区代码已存在")
public class DistrictEntity extends AbstractTreeEntity<DistrictEntity> {
    @Schema(description = "名称")
    private String name;

    @Schema(description = "代码")
    private String code;

    @Schema(description = "邮编")
    private String zipCode;

    @Schema(description = "区号")
    private String areaCode;
}
