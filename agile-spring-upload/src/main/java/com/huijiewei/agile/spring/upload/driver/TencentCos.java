package com.huijiewei.agile.spring.upload.driver;

import com.huijiewei.agile.spring.upload.UploadDriver;
import com.huijiewei.agile.spring.upload.UploadProperties;
import com.huijiewei.agile.spring.upload.UploadRequest;
import com.huijiewei.agile.spring.upload.util.UploadUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huijiewei
 */

@Component
@ConditionalOnProperty(prefix = UploadProperties.PREFIX, name = "driver-name", havingValue = TencentCos.DRIVER_NAME)
public class TencentCos implements UploadDriver {
    public static final String DRIVER_NAME = "tencent-cos";

    private final TencentCosProperties properties;

    @Autowired
    public TencentCos(TencentCosProperties properties) {
        this.properties = properties;
    }

    @Override
    public UploadRequest option(String identity, Integer size, List<String> types, List<String> thumbs, Boolean cropper) {
        String host = this.properties.getBucket() + ".cos." + this.properties.getRegion() + ".myqcloud.com";
        String url = "https://" + host + "/";
        String directory = StringUtils.stripEnd(this.properties.getDirectory(), "/") +
                "/" +
                UploadUtils.buildMonthName() +
                "/";

        String httpString = String.format("post\n%s\n\nhost=%s\n", UploadUtils.urlDecode("/"), host);

        long currentTimestamp = System.currentTimeMillis() / 1000L;

        String signTime = String.format("%d;%d", currentTimestamp - 60, currentTimestamp + 60 * 20);

        String httpStringSha1 = UploadUtils.sha1Encode(httpString);

        String signString = String.format("sha1\n%s\n%s\n", signTime, httpStringSha1);

        String signKey = this.hmacSha1(this.properties.getSecretKey(), signTime);
        String signature = this.hmacSha1(signKey, signString);

        String authorization = "q-sign-algorithm=sha1&q-ak=" +
                this.properties.getSecretId() +
                "&q-sign-time=" +
                signTime + "&q-key-time=" + signTime +
                "&q-header-list=host&q-url-param-list=&q-signature=" +
                signature;


        Map<String, String> params = new HashMap<>(3);
        params.put("key", directory + identity + "_${filename}");
        params.put("Signature", authorization);
        params.put("success_action_status", "201");

        Map<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", authorization);

        StringBuilder responseParse = new StringBuilder("var url = result.querySelector('PostResponse > Location').textContent;");

        List<UploadUtils.ThumbSize> thumbSizes = UploadUtils.getThumbSizes(thumbs);

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

        UploadRequest request = new UploadRequest();
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
