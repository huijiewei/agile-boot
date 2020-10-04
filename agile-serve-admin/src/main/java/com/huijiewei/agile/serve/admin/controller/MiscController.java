package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.app.admin.security.AdminGroupPermissionItem;
import com.huijiewei.agile.app.admin.security.AdminGroupPermissions;
import com.huijiewei.agile.app.shop.application.port.inbound.ShopCategoryUseCase;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import com.huijiewei.agile.serve.admin.security.AdminUserDetails;
import com.huijiewei.agile.spring.upload.UploadDriver;
import com.huijiewei.agile.spring.upload.UploadRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "misc", description = "杂项接口")
public class MiscController {
    private final AdminGroupPersistencePort adminGroupPersistencePort;
    private final UploadDriver uploadDriver;
    private final ShopCategoryUseCase shopCategoryUseCase;

    public MiscController(AdminGroupPersistencePort adminGroupPersistencePort, UploadDriver uploadDriver, ShopCategoryUseCase shopCategoryUseCase) {
        this.adminGroupPersistencePort = adminGroupPersistencePort;
        this.uploadDriver = uploadDriver;
        this.shopCategoryUseCase = shopCategoryUseCase;
    }

    @GetMapping(
            value = "/misc/admin-group-permissions",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "管理组 ACL 列表", operationId = "miscAdminGroupPermissions")
    @ApiResponse(responseCode = "200", description = "管理组 ACL 列表")
    public List<AdminGroupPermissionItem> actionAdminGroupPermissions() {
        return AdminGroupPermissions.getAll();
    }

    @GetMapping(
            value = "/misc/admin-groups",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "管理组列表", operationId = "miscAdminGroups")
    @ApiResponse(responseCode = "200", description = "管理组列表")
    public List<AdminGroupEntity> actionAdminGroups() {
        return this.adminGroupPersistencePort.getAll();
    }

    @GetMapping(
            value = "/misc/shop-category-tree",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "商品分类树", operationId = "miscShopCategoryTree")
    @ApiResponse(responseCode = "200", description = "商品分类树")
    public List<ShopCategoryEntity> actionShopCategoryTree() {
        return this.shopCategoryUseCase.getTree();
    }

    @GetMapping(
            value = "/misc/shop-category-path",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "商品分类路径", operationId = "miscShopCategoryPath")
    @ApiResponse(responseCode = "200", description = "商品分类路径")
    @ApiResponse(responseCode = "404", description = "分类不存在", ref = "NotFoundProblem")
    public List<ShopCategoryEntity> actionShopCategoryPath(Integer id) {
        return this.shopCategoryUseCase.getPath(id);
    }

    @GetMapping(
            value = "/misc/image-upload-option",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "图片上传设置获取", operationId = "miscImageUploadOption")
    @ApiResponse(responseCode = "200", description = "图片上传设置")
    public UploadRequest actionImageUploadOption(@RequestParam(required = false) List<String> thumbs,
                                                 @RequestParam(required = false) Boolean cropper) {
        return this.uploadDriver.build(
                "a" + AdminUserDetails.getCurrentAdminIdentity().getAdminEntity().getId().toString(),
                2048 * 1024,
                Arrays.asList("jpg", "jpeg", "gif", "png"),
                thumbs,
                cropper
        );
    }

    @GetMapping(
            value = "/misc/file-upload-option",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "文件上传设置获取", operationId = "miscFileUploadOption")
    @ApiResponse(responseCode = "200", description = "文件上传设置")
    public UploadRequest actionFileUploadOption() {
        return this.uploadDriver.build(
                "a" + AdminUserDetails.getCurrentAdminIdentity().getAdminEntity().getId().toString(),
                1024 * 1024 * 10,
                Arrays.asList("jpg", "jpeg", "gif", "png", "zip", "xlsx", "docx", "pptx")
        );
    }
}
