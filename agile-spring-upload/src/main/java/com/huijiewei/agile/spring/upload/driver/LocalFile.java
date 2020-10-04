package com.huijiewei.agile.spring.upload.driver;

import com.huijiewei.agile.spring.upload.*;
import com.huijiewei.agile.spring.upload.util.UploadUtils;
import lombok.Getter;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author huijiewei
 */

@Component
@ConditionalOnProperty(prefix = UploadProperties.PREFIX, name = "driver-name", havingValue = LocalFile.DRIVER_NAME)
public class LocalFile implements UploadDriver {
    public static final String DRIVER_NAME = "local-file";
    public final static int POLICY_DATA_LENGTH = 6;

    private final LocalFileProperties properties;

    public LocalFile(LocalFileProperties properties) {
        this.properties = properties;
    }

    private UploadPolicy parsePolicy(String policy) {
        String policyDecrypt = UploadUtils.urlDecode(policy);
        String policyValue = this.decrypt(policyDecrypt, this.properties.getPolicyKey());

        String[] policies = policyValue.split(";");

        if (policies.length != POLICY_DATA_LENGTH) {
            throw new RuntimeException("策略验证错误");
        }

        long currentTimestamp = System.currentTimeMillis() / 1000L;

        long timestamp = Long.parseLong(policies[1]);

        if (timestamp < currentTimestamp) {
            throw new RuntimeException("参数已过期");
        }

        UploadPolicy uploadPolicy = new UploadPolicy();
        uploadPolicy.setIdentity(policies[0]);
        uploadPolicy.setSize(Integer.parseInt(policies[2]));
        uploadPolicy.setTypes(Arrays.asList(policies[3].split(",")));
        uploadPolicy.setThumbs(policies[4]);
        uploadPolicy.setCropper(Boolean.parseBoolean(policies[5]));

        return uploadPolicy;
    }

    private List<UploadResponse.Thumb> thumbs(String policyThumbs, String policyIdentity, String filePath, String absoluteFilePath, String fileExtension, String absoluteUrl) {
        List<UploadUtils.ThumbSize> thumbSizes = UploadUtils.getThumbSizes(policyThumbs);

        if (thumbSizes.isEmpty()) {
            return null;
        }

        List<UploadResponse.Thumb> thumbs = new ArrayList<>();

        for (UploadUtils.ThumbSize thumbSize : thumbSizes) {
            String thumbFileName = policyIdentity + "_" + UploadUtils.random(10) + "." + fileExtension;
            String thumbFilePath = absoluteFilePath + File.separator + thumbFileName;

            try {
                Thumbnails
                        .of(filePath)
                        .size(thumbSize.getWidth(), thumbSize.getHeight())
                        .outputQuality(1.0f)
                        .toFile(thumbFilePath);
            } catch (IOException ex) {
                throw new RuntimeException("生成缩略图错误: " + ex.getMessage(), ex);
            }

            thumbs.add(new UploadResponse.Thumb(thumbSize.getThumbName(), absoluteUrl + thumbFileName));
        }

        return thumbs;
    }

    @Override
    public UploadResponse crop(String policy, ImageCropRequest request) {
        UploadPolicy uploadPolicy = this.parsePolicy(policy);

        if (!uploadPolicy.getCropper()) {
            throw new RuntimeException("不支持图片切割");
        }

        String absoluteAccessPathUrl = UploadUtils.buildAbsoluteAccessPathUrl(this.properties.getAccessPath());

        String filePath = StringUtils.stripStart(request.getFile(), absoluteAccessPathUrl);

        String absoluteUploadPath = UploadUtils.buildAbsoluteUploadPath(this.properties.getUploadPath());

        String absoluteFilePath = Paths.get(absoluteUploadPath, filePath).normalize().toString();

        File absoluteFile = new File(absoluteFilePath);

        if (!absoluteFile.exists()) {
            throw new RuntimeException("要切割的图片文件不存在");
        }

        BufferedImage image;

        try {
            image = ImageIO.read(absoluteFile);
        } catch (IOException e) {
            throw new RuntimeException("无效的图片文件");
        }

        String monthName = UploadUtils.buildMonthName();

        String absoluteCropperFilePath = absoluteUploadPath + monthName;

        File cropperFilePathFile = new File(absoluteCropperFilePath);

        if (!cropperFilePathFile.exists()) {
            if (!cropperFilePathFile.mkdirs()) {
                throw new RuntimeException("服务器创建目录错误:" + absoluteCropperFilePath);
            }
        }

        String fileExtension = FilenameUtils.getExtension(filePath);

        String fileName = uploadPolicy.getIdentity() + "_" + UploadUtils.random(10) + "." + fileExtension;

        BufferedImage cropImage = image.getSubimage(request.getX(), request.getY(), request.getW(), request.getH());

        String cropperFilePath = absoluteCropperFilePath + File.separator + fileName;

        try {
            ImageIO.write(cropImage, fileExtension, new File(cropperFilePath));
        } catch (IOException ex) {
            throw new RuntimeException("服务器保存文件错误:" + cropperFilePath, ex);
        }

        String absoluteUrl = absoluteAccessPathUrl + "/" + monthName + "/";

        return getUploadResponse(uploadPolicy, fileName, fileExtension, cropperFilePath, absoluteCropperFilePath, absoluteUrl);
    }

    private UploadResponse getUploadResponse(UploadPolicy uploadPolicy, String fileName, String fileExtension, String cropperFilePath, String absoluteCropperFilePath, String absoluteUrl) {
        UploadResponse response = new UploadResponse();

        response.setOriginal(absoluteUrl + fileName);

        List<UploadResponse.Thumb> thumbs = this.thumbs(uploadPolicy.getThumbs(), uploadPolicy.getIdentity(), cropperFilePath, absoluteCropperFilePath, fileExtension, absoluteUrl);

        if (thumbs != null) {
            response.setThumbs(thumbs);
        }

        return response;
    }

    @Override
    public UploadResponse upload(String policy, MultipartFile file) {
        UploadPolicy uploadPolicy = this.parsePolicy(policy);

        if (file.isEmpty()) {
            throw new RuntimeException("没有文件被上传");
        }

        if (file.getSize() > uploadPolicy.getSize()) {
            throw new RuntimeException("文件大小超出：" + FileUtils.byteCountToDisplaySize(uploadPolicy.getSize()));
        }

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (!uploadPolicy.getTypes().contains(fileExtension)) {
            throw new RuntimeException("文件类型限制：" + String.join(",", uploadPolicy.getTypes()));
        }

        String monthName = UploadUtils.buildMonthName();

        String absoluteUploadPath = UploadUtils.buildAbsoluteUploadPath(this.properties.getUploadPath()) +
                monthName;

        File absoluteUploadPathFile = new File(absoluteUploadPath);

        if (!absoluteUploadPathFile.exists()) {
            if (!absoluteUploadPathFile.mkdirs()) {
                throw new RuntimeException("服务器创建目录错误:" + absoluteUploadPath);
            }
        }

        String fileName = uploadPolicy.getIdentity() + "_" + UploadUtils.random(10) + "." + fileExtension;

        String uploadFilePath = absoluteUploadPath + File.separator + fileName;

        try {
            file.transferTo(Path.of(uploadFilePath));
        } catch (Exception ex) {
            throw new RuntimeException("服务器保存文件错误: " + ex.getMessage(), ex);
        }

        String absoluteUrl = UploadUtils.buildAbsoluteAccessPathUrl(this.properties.getAccessPath()) +
                "/" + monthName + "/";

        return getUploadResponse(uploadPolicy, fileName, fileExtension, uploadFilePath, absoluteUploadPath, absoluteUrl);
    }

    @Override
    public UploadRequest option(String identity, Integer size, List<String> types, List<String> thumbs, Boolean cropper) {
        long currentTimestamp = System.currentTimeMillis() / 1000L;

        String policy = String.format(
                "%s;%d;%d;%s;%s;%b",
                identity,
                currentTimestamp + 10 * 60,
                size,
                String.join(",", types),
                thumbs != null ? String.join(",", thumbs) : "",
                cropper);

        String policyEncrypt = this.encrypt(policy, this.properties.getPolicyKey());
        String policyValue = UploadUtils.urlEncode(policyEncrypt);

        UploadRequest request = new UploadRequest();
        request.setUrl(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/" + StringUtils.strip(this.properties.getUploadAction(), "/"))
                .queryParam("policy", policyValue)
                .toUriString());

        if (cropper && StringUtils.isNotEmpty(this.properties.getCorpAction())) {
            request.setCropUrl(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/" + StringUtils.strip(this.properties.getCorpAction(), "/"))
                    .queryParam("policy", policyValue)
                    .toUriString());
        }

        request.setTimeout(9 * 60);
        request.setParams(null);
        request.setHeaders(null);
        request.setDataType("json");
        request.setParamName(this.paramName());
        request.setResponseParse("return result;");

        return request;
    }

    @Override
    public String paramName() {
        return "file";
    }

    private String encrypt(String data, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));

            byte[] encrypt = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(encrypt);
        } catch (Exception ex) {
            throw new RuntimeException("Error while encrypting: " + ex.getMessage(), ex);
        }
    }

    private String decrypt(String data, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"));

            byte[] decrypt = cipher.doFinal(Base64.getDecoder().decode(data));

            return new String(decrypt);
        } catch (Exception ex) {
            throw new RuntimeException("Error while decrypting: " + ex.getMessage(), ex);
        }
    }

    @Getter
    @Setter
    public static class UploadPolicy {
        private String identity;
        private Integer size;
        private List<String> types;
        private String thumbs;
        private Boolean cropper;
    }
}
