package com.vallhalatech.profile_service.web.DTOs.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseResponse {
    private Object data;
    private String message;
    private Boolean success;
    private String timestamp;

    public static BaseResponse success(Object data, String message) {
        BaseResponse response = new BaseResponse();
        response.setData(data);
        response.setMessage(message);
        response.setSuccess(true);
        response.setTimestamp(LocalDateTime.now().toString());
        return response;
    }

    public static BaseResponse error(String message) {
        BaseResponse response = new BaseResponse();
        response.setMessage(message);
        response.setSuccess(false);
        response.setTimestamp(LocalDateTime.now().toString());
        return response;
    }
}
