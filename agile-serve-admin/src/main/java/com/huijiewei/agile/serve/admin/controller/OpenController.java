package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.core.until.HttpUtils;
import com.huijiewei.agile.spring.captcha.application.port.inbound.CaptchaUseCase;
import com.huijiewei.agile.spring.captcha.application.response.CaptchaResponse;
import com.huijiewei.agile.spring.upload.UploadService;
import com.huijiewei.agile.spring.upload.request.ImageCropRequest;
import com.huijiewei.agile.spring.upload.response.UploadResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "open", description = "开放接口")
@RequiredArgsConstructor
public class OpenController {
    private final UploadService uploadService;
    private final CaptchaUseCase captchaUseCase;

    @PostMapping(
            value = "/open/upload-file",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public UploadResponse actionUploadFile(@RequestParam("policy") String policy, @RequestPart("file") MultipartFile file) {
        return this.uploadService.upload(policy, file);
    }

    @PostMapping(
            value = "/open/crop-image",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public UploadResponse actionCropImage(@RequestParam("policy") String policy, @RequestBody ImageCropRequest request) {
        return this.uploadService.crop(policy, request);
    }

    @GetMapping(
            value = "/open/captcha"
    )
    public CaptchaResponse actionCaptcha(HttpServletRequest servletRequest) {
        return this.captchaUseCase.create(HttpUtils.getUserAgent(servletRequest), HttpUtils.getRemoteAddr(servletRequest));
    }

    @GetMapping("/open/virtual")
    public String virtual() {
        System.out.println(Thread.currentThread());
        return "虚拟线程";
    }
}
