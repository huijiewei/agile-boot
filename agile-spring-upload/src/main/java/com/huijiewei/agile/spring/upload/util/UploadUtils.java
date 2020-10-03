package com.huijiewei.agile.spring.upload.util;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author huijiewei
 */
public class UploadUtils {
    final static char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z'};
    final static String UPLOAD_PATH_FILE_PREFIX = "file:";

    public static String random(int size) {
        Random random = new Random();
        char[] cs = new char[size];
        for (int i = 0; i < cs.length; i++) {
            cs[i] = DIGITS[random.nextInt(DIGITS.length)];
        }
        return new String(cs);
    }

    public static String base64Encode(String input) {
        return UploadUtils.base64Encode(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String base64Encode(byte[] input) {
        return new String(Base64.encodeBase64(input));
    }

    public static String urlEncode(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    public static String urlDecode(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }

    public static String sha1Encode(String input) {
        return DigestUtils.sha1Hex(input);
    }

    public static String buildMonthName() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    public static String buildAbsoluteAccessPathUrl(String accessPath) {
        String replaceUrl = StringUtils
                .stripEnd(StringUtils
                        .stripEnd(accessPath, "*"), "/");

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(replaceUrl)
                .toUriString();
    }

    public static String buildAbsoluteUploadPath(String uploadPath) {
        String absolutePath;

        if (StringUtils.startsWith(uploadPath, UPLOAD_PATH_FILE_PREFIX)) {
            absolutePath = StringUtils.stripStart(uploadPath, UPLOAD_PATH_FILE_PREFIX);
        } else {
            absolutePath = Paths.get(
                    new ApplicationHome(UploadUtils.class)
                            .getSource()
                            .getParentFile()
                            .toString(),
                    uploadPath)
                    .toAbsolutePath()
                    .normalize()
                    .toString();
        }

        return StringUtils.stripEnd(absolutePath, File.separator) + File.separator;
    }

    public static List<ThumbSize> getThumbSizes(List<String> thumbs) {
        List<ThumbSize> thumbSizes = new ArrayList<>();

        if (thumbs == null) {
            return thumbSizes;
        }

        for (String thumb : thumbs) {
            if (StringUtils.isNotEmpty(thumb)) {
                String[] thumbSize = thumb.split("x");

                if (thumbSize.length == 2) {
                    int width = Integer.parseInt(thumbSize[0]);
                    int height = Integer.parseInt(thumbSize[1]);

                    if (width > 0 && height > 0) {
                        thumbSizes.add(ThumbSize.of(width, height));
                    }
                }
            }
        }

        return thumbSizes;
    }

    public static List<ThumbSize> getThumbSizes(String thumbsPolicy) {
        if (StringUtils.isEmpty(thumbsPolicy)) {
            return new ArrayList<>();
        }

        return UploadUtils.getThumbSizes(Arrays.asList(thumbsPolicy.split(",")));
    }

    @Getter
    @Setter
    public static class ThumbSize {
        private String thumbName;
        private Integer width;
        private Integer height;

        public static ThumbSize of(Integer width, Integer height) {
            ThumbSize thumbSize = new ThumbSize();
            thumbSize.width = width;
            thumbSize.height = height;
            thumbSize.thumbName = width.toString() + "x" + height.toString();

            return thumbSize;
        }
    }
}
