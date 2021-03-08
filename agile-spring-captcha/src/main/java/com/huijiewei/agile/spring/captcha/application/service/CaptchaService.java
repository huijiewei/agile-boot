package com.huijiewei.agile.spring.captcha.application.service;

import com.devskiller.friendly_id.FriendlyId;
import com.huijiewei.agile.spring.captcha.CaptchaProperties;
import com.huijiewei.agile.spring.captcha.application.port.inbound.CaptchaUseCase;
import com.huijiewei.agile.spring.captcha.application.port.outbound.CaptchaPersistencePort;
import com.huijiewei.agile.spring.captcha.application.response.CaptchaResponse;
import com.huijiewei.agile.spring.captcha.domain.CaptchaEntity;
import com.wf.captcha.GifCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author huijiewei
 */

@Service
public class CaptchaService implements CaptchaUseCase {
    private final static String CAPTCHA_REQUEST_SPLIT = "-";

    private final CaptchaPersistencePort captchaPersistencePort;
    private final CaptchaProperties captchaProperties;

    public CaptchaService(CaptchaPersistencePort captchaPersistencePort, CaptchaProperties captchaProperties) {
        this.captchaPersistencePort = captchaPersistencePort;
        this.captchaProperties = captchaProperties;
    }

    @Override
    public Boolean verify(String captcha, String userAgent, String remoteAddr) {
        if (StringUtils.isBlank(captcha)) {
            return false;
        }

        var captchaSplit = captcha.split(CAPTCHA_REQUEST_SPLIT);

        if (captchaSplit.length != 2) {
            return false;
        }

        if (StringUtils.isBlank(captchaSplit[0]) || StringUtils.isBlank(captchaSplit[1])) {
            return false;
        }

        var captchaEntityOptional =
                this.captchaPersistencePort.getByCode(captchaSplit[0], captchaSplit[1], userAgent, remoteAddr);

        if (captchaEntityOptional.isEmpty()) {
            return false;
        }

        var captchaEntity = captchaEntityOptional.get();

        this.captchaPersistencePort.deleteById(captchaEntity.getId());

        return !captchaEntity.isExpired();
    }

    @Override
    public CaptchaResponse create(String userAgent, String remoteAddr) {
        var uuid = FriendlyId.createFriendlyId();

        var gifCaptcha = new GifCaptcha(captchaProperties.getWidth(),
                captchaProperties.getHeight(),
                captchaProperties.getLength());

        var captchaEntity = new CaptchaEntity();
        captchaEntity.setCode(gifCaptcha.text());
        captchaEntity.setUuid(uuid);
        captchaEntity.setUserAgent(userAgent);
        captchaEntity.setRemoteAddr(remoteAddr);

        this.captchaPersistencePort.save(captchaEntity);

        var captchaResponse = new CaptchaResponse();
        captchaResponse.setImage(gifCaptcha.toBase64());
        captchaResponse.setProcess("return captcha + '" + CAPTCHA_REQUEST_SPLIT + uuid + "'");

        return captchaResponse;
    }
}
