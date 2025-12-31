package com.huijiewei.agile.spring.captcha.adapter.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

/**
 * @author huijiewei
 */

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Captcha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uuid;

    private String code;

    private String userAgent;

    private String remoteAddr;

    @Column(updatable = false)
    private LocalDateTime createdAt;
}
