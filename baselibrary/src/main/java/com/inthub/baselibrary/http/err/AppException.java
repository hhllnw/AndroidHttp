package com.inthub.baselibrary.http.err;

/**
 * Created by hhl on 2016/8/17.
 * 异常统一处理
 */
public class AppException extends Exception {
    private int statusCode;
    private String responseMessage;
    private ErrType type;

    public enum ErrType {CANCEL, TIME_OUT, SERVER, MANUAL, JSON, UPLOAD, IO}

    public AppException(ErrType type, String detailMessage) {
        super(detailMessage);
        this.type = type;
    }

    public AppException(int statusCode, String responseMessage) {
        super(responseMessage);
        this.type = ErrType.SERVER;
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ErrType getType() {
        return type;
    }

    public void setType(ErrType type) {
        this.type = type;
    }
}
