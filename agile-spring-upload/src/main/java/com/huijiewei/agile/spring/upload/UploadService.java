package com.huijiewei.agile.spring.upload;

import com.huijiewei.agile.spring.upload.request.ImageCropRequest;
import com.huijiewei.agile.spring.upload.request.UploadRequest;
import com.huijiewei.agile.spring.upload.response.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author huijiewei
 */

public interface UploadService {
    /**
     * 构建上传配置
     *
     * @param identity 标识
     * @param size     上传文件大小
     * @param types    上传文件后缀
     * @return 上传配置
     */
    default UploadRequest build(String identity, Integer size, List<String> types) {
        return this.build(identity, size, types, null);
    }

    /**
     * 构建上传配置
     *
     * @param identity 标识
     * @param size     上传文件大小
     * @param types    上传文件后缀
     * @param thumbs   缩略图
     * @return 上传配置
     */
    default UploadRequest build(String identity, Integer size, List<String> types, List<String> thumbs) {
        UploadRequest request = this.option(identity, size, types, thumbs, false);

        request.setSizeLimit(size);
        request.setTypesLimit(types);

        return request;
    }

    /**
     * 构建上传配置
     *
     * @param identity 标识
     * @param size     上传文件大小
     * @param types    上传文件后缀
     * @param thumbs   缩略图
     * @param cropper  是否可以切图
     * @return 上传配置
     */
    default UploadRequest build(String identity, Integer size, List<String> types, List<String> thumbs, Boolean cropper) {
        UploadRequest request = this.option(identity, size, types, thumbs, cropper != null && cropper);

        request.setSizeLimit(size);
        request.setTypesLimit(types);

        return request;
    }

    /**
     * 构建上传配置
     *
     * @param identity 标识
     * @param size     上传文件大小
     * @param types    上传文件后缀
     * @param thumbs   缩略图
     * @param cropper  是否可以切图
     * @return 上传配置
     */
    UploadRequest option(String identity, Integer size, List<String> types, List<String> thumbs, Boolean cropper);

    /**
     * 文件参数名
     *
     * @return 文件参数名
     */
    String paramName();

    /**
     * 文件上传
     *
     * @param policy 策略
     * @param file   MultipartFile
     * @return UploadResponse
     */
    default UploadResponse upload(String policy, MultipartFile file) {
        throw new RuntimeException("方法未实现");
    }

    /**
     * 图片切割
     *
     * @param policy  策略
     * @param request ImageCropRequest
     * @return UploadResponse
     */
    default UploadResponse crop(String policy, ImageCropRequest request) {
        throw new RuntimeException("方法未实现");
    }
}
