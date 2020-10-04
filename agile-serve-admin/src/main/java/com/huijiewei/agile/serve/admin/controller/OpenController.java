package com.huijiewei.agile.serve.admin.controller;

import com.huijiewei.agile.app.open.application.port.CaptchaPersistencePort;
import com.huijiewei.agile.app.open.domain.CaptchaEntity;
import com.huijiewei.agile.core.exception.BadRequestException;
import com.huijiewei.agile.core.until.HttpUtils;
import com.huijiewei.agile.spring.upload.ImageCropRequest;
import com.huijiewei.agile.spring.upload.UploadDriver;
import com.huijiewei.agile.spring.upload.UploadResponse;
import com.wf.captcha.GifCaptcha;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huijiewei
 */

@RestController
@Tag(name = "open", description = "开放接口")
public class OpenController {
    private final UploadDriver uploadDriver;
    private final CaptchaPersistencePort captchaPersistencePort;

    public OpenController(UploadDriver uploadDriver, CaptchaPersistencePort captchaPersistencePort) {
        this.uploadDriver = uploadDriver;
        this.captchaPersistencePort = captchaPersistencePort;
    }

    @PostMapping(
            value = "/open/upload-file",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public UploadResponse actionUploadFile(@RequestParam("policy") String policy, @RequestPart("file") MultipartFile file) {
        return this.uploadDriver.upload(policy, file);
    }

    @PostMapping(
            value = "/open/crop-image",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public UploadResponse actionCropImage(@RequestParam("policy") String policy, @RequestBody ImageCropRequest request) {
        return this.uploadDriver.crop(policy, request);
    }

    @GetMapping(
            value = "/open/captcha"
    )
    public String actionCaptcha(@RequestParam("uuid") String uuid, HttpServletRequest servletRequest) {
        if (StringUtils.isEmpty(uuid)) {
            throw new BadRequestException("参数错误");
        }

        CaptchaEntity captchaEntity = new CaptchaEntity();

        captchaEntity.setUuid(uuid);
        captchaEntity.setUserAgent(HttpUtils.getUserAgent(servletRequest));
        captchaEntity.setRemoteAddr(HttpUtils.getRemoteAddr(servletRequest));

        GifCaptcha gifCaptcha = new GifCaptcha(100, 30, 5);

        captchaEntity.setCode(gifCaptcha.text());

        this.captchaPersistencePort.save(captchaEntity);

        return gifCaptcha.toBase64();
    }
}
