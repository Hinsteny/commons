package com.github.hinsteny.commons.warp.http.exception;

/**
 * http请求异常类.
 *
 * @author Hinsteny
 * @version HttpException: 2019-08-12 11:32 All rights reserved.$
 */
public class HttpException extends Exception {

    private String requestId;

    /**
     * 封装后的本地错误信息
     */
    private String errorCode;
    private String errorMsg;

    /**
     * 标注渠道方的错误信息
     */
    private String extraCode;
    private String extraMsg;

    public HttpException(String requestId, String errorCode, String errorMsg) {
        this.requestId = requestId;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public HttpException(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public HttpException(String requestId, String errorCode, String errorMsg, String extraCode, String extraMsg) {
        this.extraMsg = extraMsg;
        this.requestId = requestId;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.extraCode = extraCode;
    }

    public HttpException(HttpErrorCode httpErrorCode) {
        this.errorCode = httpErrorCode.getCode();
        this.errorMsg = httpErrorCode.getMessage();
    }

    public HttpException(HttpErrorCode httpErrorCode, String extendMsg) {
        this.errorCode = httpErrorCode.getCode();
        this.errorMsg = String.format(httpErrorCode.getMessage(), extendMsg);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getExtraCode() {
        return extraCode;
    }

    public void setExtraCode(String extraCode) {
        this.extraCode = extraCode;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }

    @Override
    public String toString() {
        return "HttpException{" +
            "requestId='" + requestId + '\'' +
            ", errorCode='" + errorCode + '\'' +
            ", errorMsg='" + errorMsg + '\'' +
            ", extraCode='" + extraCode + '\'' +
            ", extraMsg='" + extraMsg + '\'' +
            "} " + super.toString();
    }
}
