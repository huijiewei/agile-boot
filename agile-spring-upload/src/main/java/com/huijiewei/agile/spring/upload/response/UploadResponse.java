package com.huijiewei.agile.spring.upload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author huijiewei
 */
@Getter
@Setter
public class UploadResponse {
    private String original;

    private List<Thumb> thumbs;

    @Getter
    @Setter
    public static class Thumb {
        private String thumb;
        private String url;

        public Thumb(String thumb, String url) {
            this.setThumb(thumb);
            this.setUrl(url);
        }
    }
}
