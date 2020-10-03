package com.huijiewei.agile.spring.upload;

import lombok.Data;

/**
 * @author huijiewei
 */

@Data
public class ImageCropRequest {
    private String file;
    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;
}
