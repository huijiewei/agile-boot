package com.huijiewei.agile.app.user.domain;

import com.huijiewei.agile.app.district.domain.DistrictEntity;
import com.huijiewei.agile.core.domain.AbstractEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huijiewei
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAddressEntity extends AbstractEntity {

    @Schema(description = "联系人")
    private String name;

    @Schema(description = "地址别名")
    private String alias;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "所属用户 Id")
    private Integer userId;

    @Schema(description = "所属用户")
    private UserEntity user;

    @Schema(description = "区域代码")
    private String districtCode;

    @Schema(description = "所属区域路径")
    private List<DistrictEntity> districtPath;

    @Schema(description = "区域地址")
    public String getDistrictAddress() {
        return this.getDistrictPath()
                .stream()
                .filter(districtEntity -> !districtEntity.getName().equals("市辖区"))
                .map(DistrictEntity::getName)
                .collect(Collectors.joining());
    }

    @Schema(description = "地址全路径")
    public String getFullAddress() {
        return this.getDistrictAddress() + this.getAddress();
    }
}
