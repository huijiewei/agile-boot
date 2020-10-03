package com.huijiewei.agile.spring.upload;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author huijiewei
 */

@Data
public class UploadRequest {
    private String url;

    private String cropUrl;

    private Integer timeout;

    private String paramName;

    private String dataType;

    private Map<String, String> params;

    private Map<String, String> headers;

    private String responseParse;

    private Integer sizeLimit;

    private List<String> typesLimit;
}
