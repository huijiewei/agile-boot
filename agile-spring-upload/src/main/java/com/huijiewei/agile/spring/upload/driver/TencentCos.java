package com.huijiewei.agile.spring.upload.driver;

import com.huijiewei.agile.spring.upload.UploadProperties;
import com.huijiewei.agile.spring.upload.UploadService;
import com.huijiewei.agile.spring.upload.request.UploadRequest;
import com.huijiewei.agile.spring.upload.util.UploadUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author huijiewei
 */

@Component
@ConditionalOnProperty(prefix = UploadProperties.PREFIX, name = "driver-name", havingValue = TencentCos.DRIVER_NAME)
public class TencentCos implements UploadService {
    public static final String DRIVER_NAME = "tencent-cos";

    private final TencentCosProperties properties;

    public TencentCos(TencentCosProperties properties) {
        this.properties = properties;
    }

    @Override
    public UploadRequest option(String identity, Integer size, List<String> types, List<String> thumbs, Boolean cropper) {
        var host = this.properties.getBucket() + ".cos." + this.properties.getRegion() + ".myqcloud.com";
        var url = "https://" + host + "/";
        var directory = StringUtils.stripEnd(this.properties.getDirectory(), "/") +
                "/" +
                UploadUtils.buildMonthName() +
                "/";

        var httpString = String.format("post\n%s\n\nhost=%s\n", UploadUtils.urlDecode("/"), host);

        var currentTimestamp = System.currentTimeMillis() / 1000L;

        var signTime = String.format("%d;%d", currentTimestamp - 60, currentTimestamp + 60 * 20);

        var httpStringSha1 = UploadUtils.sha1Encode(httpString);

        var signString = String.format("sha1\n%s\n%s\n", signTime, httpStringSha1);

        var signKey = this.hmacSha1(this.properties.getSecretKey(), signTime);
        var signature = this.hmacSha1(signKey, signString);

        var authorization = "q-sign-algorithm=sha1&q-ak=" +
                this.properties.getSecretId() +
                "&q-sign-time=" +
                signTime + "&q-key-time=" + signTime +
                "&q-header-list=host&q-url-param-list=&q-signature=" +
                signature;


        var params = new HashMap<String, String>(3);
        params.put("key", directory + identity + "_${filename}");
        params.put("Signature", authorization);
        params.put("success_action_status", "201");

        var headers = new HashMap<String, String>(1);
        headers.put("Authorization", authorization);

        var responseParse = new StringBuilder("var url = result.querySelector('PostResponse > Location').textContent;");

        var thumbSizes = UploadUtils.getThumbSizes(thumbs);

        if (thumbSizes.isEmpty()) {
            responseParse.append("var thumbs = null;");
        } else {
            responseParse.append("var thumbs = [];");

            for (UploadUtils.ThumbSize thumbSize : thumbSizes) {
                responseParse.append("thumbs.push({ thumb: '")
                        .append(thumbSize.getThumbName())
                        .append("', url: url + '")
                        .append(this.properties.getStyleDelimiter())
                        .append(thumbSize.getThumbName())
                        .append("'});");
            }
        }

        responseParse.append("return { original: url, thumbs: thumbs }; ");

        var request = new UploadRequest();
        request.setUrl(url);
        request.setTimeout(19 * 60);
        request.setParams(params);
        request.setHeaders(headers);
        request.setDataType("xml");
        request.setParamName(this.paramName());
        request.setResponseParse(responseParse.toString());

        return request;
    }

    @Override
    public String paramName() {
        return "file";
    }

    private String hmacSha1(String key, String input) {
        return new HmacUtils("HmacSHA1", key).hmacHex(input);
    }
}
