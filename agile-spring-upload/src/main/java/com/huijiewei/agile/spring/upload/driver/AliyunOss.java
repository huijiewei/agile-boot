package com.huijiewei.agile.spring.upload.driver;

import com.huijiewei.agile.spring.upload.UploadProperties;
import com.huijiewei.agile.spring.upload.UploadService;
import com.huijiewei.agile.spring.upload.request.UploadRequest;
import com.huijiewei.agile.spring.upload.util.UploadUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * @author huijiewei
 */

@Component
@ConditionalOnProperty(prefix = UploadProperties.PREFIX, name = "driver-name", havingValue = AliyunOss.DRIVER_NAME)
public class AliyunOss implements UploadService {
    public static final String DRIVER_NAME = "aliyun-oss";

    private final AliyunOssProperties properties;

    public AliyunOss(AliyunOssProperties properties) {
        this.properties = properties;
    }

    @Override
    public UploadRequest option(String identity, Integer size, List<String> types, List<String> thumbs, Boolean cropper) {
        var url = "https://" + this.properties.getBucket() + "." + this.properties.getEndpoint();
        var directory = StringUtils.stripEnd(this.properties.getDirectory(), "/") +
                "/" +
                UploadUtils.buildMonthName() +
                "/";

        var jsonExpiration = String.format(
                "\"expiration\":\"%s\"",
                LocalDateTime.now().plusMinutes(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
        );

        var jsonContentLengthRange = String.format("[\"content-length-range\",%d,%d]", 0, size);
        var jsonStartWith = String.format("[\"starts-with\",\"$key\",\"%s\"]", directory);

        var jsonConditions = String.format(
                "\"conditions\":[%s,%s]",
                jsonContentLengthRange,
                jsonStartWith
        );

        var policyJson = String.format("{%s,%s}", jsonExpiration, jsonConditions);

        var policyString = UploadUtils.base64Encode(policyJson);

        var signature = UploadUtils.base64Encode(this.hmacSha1(this.properties.getAccessKeySecret(), policyString));

        var params = new HashMap<String, String>(5);
        params.put("OSSAccessKeyId", this.properties.getAccessKeyId());
        params.put("key", directory + identity + "_${filename}");
        params.put("policy", policyString);
        params.put("signature", signature);
        params.put("success_action_status", "201");

        var responseParse = new StringBuilder("var url = result.querySelector('PostResponse > Location').textContent;");

        var thumbSizes = UploadUtils.getThumbSizes(thumbs);

        if (thumbSizes.isEmpty()) {
            responseParse.append("var thumbs = null;");
        } else {
            responseParse.append("var thumbs = [];");

            var styleDelimiter = StringUtils.isBlank(this.properties.getStyleDelimiter()) ? "?x-oss-process=style/" : this.properties.getStyleDelimiter();

            for (UploadUtils.ThumbSize thumbSize : thumbSizes) {
                responseParse.append("thumbs.push({ thumb: '")
                        .append(thumbSize.getThumbName())
                        .append("', url: url + '")
                        .append(styleDelimiter)
                        .append(thumbSize.getThumbName())
                        .append("'});");
            }
        }

        responseParse.append("return { original: url, thumbs: thumbs }; ");

        var request = new UploadRequest();
        request.setUrl(url);
        request.setTimeout(9 * 60);
        request.setParams(params);
        request.setHeaders(null);
        request.setDataType("xml");
        request.setParamName(this.paramName());
        request.setResponseParse(responseParse.toString());

        return request;
    }

    @Override
    public String paramName() {
        return "file";
    }

    private byte[] hmacSha1(String key, String input) {
        try {
            var mac = Mac.getInstance("HmacSHA1");

            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));

            return mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unsupported algorithm: HmacSHA1");
        } catch (InvalidKeyException ex) {
            throw new RuntimeException("Invalid key: " + key);
        }
    }
}
