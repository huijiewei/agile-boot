package com.huijiewei.agile.spring.captcha.application.service;

import com.devskiller.friendly_id.FriendlyId;
import com.huijiewei.agile.spring.captcha.CaptchaProperties;
import com.huijiewei.agile.spring.captcha.application.port.inbound.CaptchaUseCase;
import com.huijiewei.agile.spring.captcha.application.port.outbound.CaptchaPersistencePort;
import com.huijiewei.agile.spring.captcha.application.request.CaptchaRequest;
import com.huijiewei.agile.spring.captcha.application.response.CaptchaResponse;
import com.huijiewei.agile.spring.captcha.domain.CaptchaEntity;
import com.wf.captcha.GifCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author huijiewei
 */

@Service
public class CaptchaService implements CaptchaUseCase {
    private final CaptchaPersistencePort captchaPersistencePort;
    private final CaptchaProperties captchaProperties;

    public CaptchaService(CaptchaPersistencePort captchaPersistencePort, CaptchaProperties captchaProperties) {
        this.captchaPersistencePort = captchaPersistencePort;
        this.captchaProperties = captchaProperties;
    }

    @Override
    public Boolean verify(CaptchaRequest request, String userAgent, String remoteAddr) {
        if (request == null || StringUtils.isBlank(request.getUuid()) || StringUtils.isBlank(request.getCode())) {
            return false;
        }

        Optional<CaptchaEntity> captchaEntityOptional = this.captchaPersistencePort.getByCode(request.getCode(), request.getUuid(), userAgent, remoteAddr);

        if (captchaEntityOptional.isEmpty()) {
            return false;
        }

        CaptchaEntity captchaEntity = captchaEntityOptional.get();

        this.captchaPersistencePort.deleteById(captchaEntity.getId());

        return !captchaEntity.isExpired();
    }

    @Override
    public CaptchaResponse create(String userAgent, String remoteAddr) {
        String uuid = FriendlyId.createFriendlyId();

        GifCaptcha gifCaptcha = new GifCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight(), captchaProperties.getLength());

        CaptchaEntity captchaEntity = new CaptchaEntity();
        captchaEntity.setCode(gifCaptcha.text());
        captchaEntity.setUuid(uuid);
        captchaEntity.setUserAgent(userAgent);
        captchaEntity.setRemoteAddr(remoteAddr);

        this.captchaPersistencePort.save(captchaEntity);

        CaptchaResponse captchaResponse = new CaptchaResponse();
        captchaResponse.setUuid(uuid);
        captchaResponse.setCaptcha(gifCaptcha.toBase64());
        captchaResponse.setExpiredAt(LocalDateTime.now().plusSeconds(CaptchaEntity.CAPTCHA_EXPIRED_SECONDS));

        return captchaResponse;
    }
}
