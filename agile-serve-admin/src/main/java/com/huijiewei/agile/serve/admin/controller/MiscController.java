package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.admin.application.port.outbound.AdminGroupPersistencePort;
import com.huijiewei.agile.app.admin.domain.AdminGroupEntity;
import com.huijiewei.agile.app.admin.security.AdminGroupPermissionItem;
import com.huijiewei.agile.app.admin.security.AdminGroupPermissions;
import com.huijiewei.agile.app.cms.application.port.inbound.CmsCategoryUseCase;
import com.huijiewei.agile.app.cms.domain.CmsCategoryEntity;
import com.huijiewei.agile.app.district.application.port.inbound.DistrictUseCase;
import com.huijiewei.agile.app.district.domain.DistrictEntity;
import com.huijiewei.agile.app.shop.application.port.inbound.ShopCategoryUseCase;
import com.huijiewei.agile.app.shop.domain.ShopCategoryEntity;
import com.huijiewei.agile.serve.admin.security.AdminUserDetails;
import com.huijiewei.agile.spring.upload.UploadService;
import com.huijiewei.agile.spring.upload.request.UploadRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MiscController {
    private final AdminGroupPersistencePort adminGroupPersistencePort;
    private final UploadService uploadService;
    private final ShopCategoryUseCase shopCategoryUseCase;
    private final DistrictUseCase districtUseCase;
    private final CmsCategoryUseCase cmsCategoryUseCase;

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
        return this.shopCategoryUseCase.loadTree();
    }

    @GetMapping(
            value = "/misc/shop-category-path",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "商品分类路径", operationId = "miscShopCategoryPath")
    @ApiResponse(responseCode = "200", description = "商品分类路径")
    @ApiResponse(responseCode = "404", description = "分类不存在", ref = "NotFoundProblem")
    public List<ShopCategoryEntity> actionShopCategoryPath(Integer id) {
        return this.shopCategoryUseCase.loadPathById(id);
    }

    @GetMapping(
            value = "/misc/cms-category-tree",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "内容分类树", operationId = "miscCmsCategoryTree")
    @ApiResponse(responseCode = "200", description = "商品分类树")
    public List<CmsCategoryEntity> actionCmsCategoryTree() {
        return this.cmsCategoryUseCase.loadTree();
    }

    @GetMapping(
            value = "/misc/cms-category-path",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "内容分类路径", operationId = "miscCmsCategoryPath")
    @ApiResponse(responseCode = "200", description = "内容分类路径")
    @ApiResponse(responseCode = "404", description = "分类不存在", ref = "NotFoundProblem")
    public List<CmsCategoryEntity> actionCmsCategoryPath(Integer id) {
        return this.cmsCategoryUseCase.loadPathById(id);
    }

    @GetMapping(
            value = "/misc/districts",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "地区列表", operationId = "miscDistricts")
    @ApiResponse(responseCode = "200", description = "地区列表")
    public List<DistrictEntity> actionDistricts(@RequestParam() Integer parentId) {
        return this.districtUseCase.loadByParentId(parentId);
    }

    @GetMapping(
            value = "/misc/district-path",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "地区路径", operationId = "miscDistrictPath")
    @ApiResponse(responseCode = "200", description = "地区路径")
    @ApiResponse(responseCode = "404", description = "地区不存在", ref = "NotFoundProblem")
    public List<DistrictEntity> actionDistrictPath(Integer id) {
        return this.districtUseCase.loadPathById(id);
    }

    @GetMapping(
            value = "/misc/district-search-tree",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "搜索地区树", operationId = "miscDistrictSearchTree")
    @ApiResponse(responseCode = "200", description = "地区路径")
    public List<DistrictEntity> actionDistrictSearchPath(@RequestParam() String keyword) {
        return this.districtUseCase.loadTreeByKeyword(keyword);
    }

    @GetMapping(
            value = "/misc/image-upload-option",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Operation(description = "图片上传设置获取", operationId = "miscImageUploadOption")
    @ApiResponse(responseCode = "200", description = "图片上传设置")
    public UploadRequest actionImageUploadOption(@RequestParam(required = false) List<String> thumbs,
                                                 @RequestParam(required = false) Boolean cropper) {
        return this.uploadService.build(
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
        return this.uploadService.build(
                "a" + AdminUserDetails.getCurrentAdminIdentity().getAdminEntity().getId().toString(),
                1024 * 1024 * 10,
                Arrays.asList("jpg", "jpeg", "gif", "png", "zip", "xlsx", "docx", "pptx")
        );
    }
}
