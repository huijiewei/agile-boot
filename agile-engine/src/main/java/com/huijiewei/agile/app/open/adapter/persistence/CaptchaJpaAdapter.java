package com.huijiewei.agile.app.open.adapter.persistence;

import com.huijiewei.agile.app.open.adapter.persistence.mapper.CaptchaMapper;
import com.huijiewei.agile.app.open.adapter.persistence.repository.CaptchaJpaRepository;
import com.huijiewei.agile.app.open.application.port.CaptchaPersistencePort;
import com.huijiewei.agile.app.open.domain.CaptchaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author huijiewei
 */

@Component
@Transactional(readOnly = true)
public class CaptchaJpaAdapter implements CaptchaPersistencePort {
    private final CaptchaMapper captchaMapper;
    private final CaptchaJpaRepository captchaJpaRepository;

    public CaptchaJpaAdapter(CaptchaMapper captchaMapper, CaptchaJpaRepository captchaJpaRepository) {
        this.captchaMapper = captchaMapper;
        this.captchaJpaRepository = captchaJpaRepository;
    }

    @Override
    public Optional<CaptchaEntity> getByCode(String code, String uuid, String userAgent, String remoteAddr) {
        return this.captchaJpaRepository
                .findByCodeAndUuidAndUserAgentAndRemoteAddr(code, uuid, userAgent, remoteAddr)
                .map(this.captchaMapper::toCaptchaEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        this.captchaJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(CaptchaEntity captchaEntity) {
        return this.captchaJpaRepository.save(this.captchaMapper.toCaptcha(captchaEntity)).getId();
    }
}
