package com.huijiewei.agile.app.user.application.request;

import com.huijiewei.agile.core.application.request.AbstractSearchRequest;
import com.huijiewei.agile.core.application.request.KeywordSearchField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huijiewei
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class UserAddressSearchRequest extends AbstractSearchRequest {
    private String name;
    private String phone;
    private String userName;
    private String userPhone;
    private String userEmail;

    private String districtName;

    public UserAddressSearchRequest() {
        this
                .addSearchField(new KeywordSearchField().field("name").label("联系人"))
                .addSearchField(new KeywordSearchField().field("phone").label("联系方式"))
                .addSearchField(new KeywordSearchField().field("userName").label("用户名称"))
                .addSearchField(new KeywordSearchField().field("userPhone").label("用户手机号码"))
                .addSearchField(new KeywordSearchField().field("userEmail").label("用户电子邮箱"))
                .addSearchField(new KeywordSearchField().field("districtName").label("区域名称"));
    }
}
