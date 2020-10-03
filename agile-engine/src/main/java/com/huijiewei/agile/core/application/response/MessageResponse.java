package com.huijiewei.agile.core.application.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author huijiewei
 */

@Getter
@Setter
public class MessageResponse {
    private String message;

    public static MessageResponse of(String message) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage(message);
        return messageResponse;
    }
}
