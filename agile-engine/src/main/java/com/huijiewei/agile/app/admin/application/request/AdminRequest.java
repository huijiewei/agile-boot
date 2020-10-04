package com.huijiewei.agile.app.admin.application.request;

import com.huijiewei.agile.app.admin.application.service.AdminGroupExistsService;
import com.huijiewei.agile.core.constraint.Exists;
import com.huijiewei.agile.core.constraint.FieldMatch;
import com.huijiewei.agile.core.constraint.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;

/**
 * @author huijiewei
 */

@Getter
@Setter
@FieldMatch(field = "password", fieldMatch = "passwordConfirm", message = "密码与密码确认必须相同")
public class AdminRequest {
    @NotBlank(message = "手机号码不能为空")
    @Phone(message = "无效的手机号码")
    private String phone;

    @NotBlank(message = "电子邮箱不能为空")
    @Email(message = "无效的电子邮箱")
    private String email;

    @NotBlank(message = "密码不能为空", groups = OnCreate.class)
    private String password;

    @NotBlank(message = "密码确认不能为空", groups = OnCreate.class)
    private String passwordConfirm;

    @NotNull
    private String name;

    @NotNull
    private String avatar;

    @Positive(message = "请选择管理组")
    @Exists(existService = AdminGroupExistsService.class, targetProperty = "id", message = "你选择的管理组不存在")
    @Schema(description = "所在管理组", required = true)
    private Integer adminGroupId;

    public interface OnCreate extends Default {
    }

    public interface OnUpdate extends Default {
    }
}
