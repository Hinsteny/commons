package com.github.hinsteny.commons.core.exception;

/**
 * 业务异常类.
 *
 * @author Hinsteny
 * @version BusinessException: 2019-08-12 11:26 All rights reserved.$
 */
public class BusinessException extends Exception {

    /**
     * 业务异常码
     */
    private String code;

    /**
     * 业务异常信息
     */
    private String msg;

    /**
     * Constructs a new exception with {@code null} as its detail message. The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public BusinessException(String code, String msg) {
        this(code, msg, null);
    }

    /**
     * Constructs a new exception with {@code null} as its detail message. The cause is not initialized, and may subsequently be initialized by a call to {@link #initCause}.
     */
    public BusinessException(String code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
        this.msg = msg;
    }

    /**
     * Constructs a new exception with the specified detail message and cause.  <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically incorporated in this
     * exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
     * @since 12
     */
    public BusinessException(String message, Throwable cause, String code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
            "code='" + code + '\'' +
            ", msg='" + msg + '\'' +
            "} " + super.toString();
    }

}
