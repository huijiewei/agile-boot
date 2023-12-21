package com.huijiewei.agile.app.user.application.request;

import com.huijiewei.agile.app.district.application.service.DistrictExistsService;
import com.huijiewei.agile.app.user.application.service.UserExistsService;
import com.huijiewei.agile.core.constraint.Exists;
import com.huijiewei.agile.core.constraint.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * @author huijiewei
 */

@Data
public class UserAddressRequest {
    @NotBlank(message = "地址别名不能为空")
    private String alias;

    @NotBlank(message = "联系人不能为空")
    private String name;

    @NotBlank(message = "联系方式不能为空")
    @Phone(message = "无效的联系方式")
    private String phone;

    @NotBlank(message = "详细地址不能为空")
    private String address;

    @Positive(message = "所属用户不能为空")
    @Exists(existService = UserExistsService.class, targetProperty = "id", message = "所属用户不存在")
    @Schema(description = "所属用户", required = true)
    private Integer userId;

    @NotBlank(message = "所在区域不能为空")
    @Exists(existService = DistrictExistsService.class, targetProperty = "code", message = "所在区域不存在")
    @Schema(description = "所在区域", required = true)
    private String districtCode;
}
