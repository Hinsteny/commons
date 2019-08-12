package com.github.hinsteny.commons.warp.http.exception;

/**
 * 错误码枚举(XYZZZ).
 *
 * @author Hinsteny
 * @version HttpErrorCode: 2019-08-12 11:34 All rights reserved.$
 */
public enum HttpErrorCode {

    /**
     * 1开头为客户端异常
     */
    CREATE_CLIENT_ERROR("11000", "创建客户端异常，未定义"),
    CREATE_PROTOCOL_REQUEST_ERROR("11001", "创建通信协议请求异常，未定义"),
    CREATE_SSL_CONNECTION_ERROR("11002", "创建SSL连接异常"),
    CHECK_REQUEST_ERROR("11003", "校验Request不通过，协议不匹配"),
    CHECK_PROFILE_ERROR("11004", "校验Profile不通过，协议不匹配"),
    GET_PARAM_STRING_ERROR("11005", "转换Map参数错误"),
    METHOD_TYPE_NOT_SUPPORT("11006", "方法类型不支持"),
    PARAM_TYPE_NOT_SUPPORT("11007", "方法参数类型不支持"),
    BEAN_CONVERT_TO_MAP_ERROR("11008", "Http请求Bean转换成Map异常"),
    ID_MAX_LENGTH_ERROR("11009", "生成的ID超过最大限制"),
    PARAM_SIGN_ERROR("11010", "参数签名异常"),
    PARAM_ENCRYPT_ERROR("11011", "参数加密异常"),
    ASK_SERVICE_INVALID("11012", "http请求资源无效[地址, 服务不存在]"),
    PARAM_URLENCODE_ERROR("11013", "请求参数类型URL编码错误"),

    RESPONSE_FORMAT_ERROR("12000", "响应报文格式异常"),
    RESPONSE_EMPTY_ERROR("12001", "响应报文为空"),
    PARSE_RESULT_ERROR("12002", "解析返回值错误"),
    VERIFY_SIGN_ERROR("12003", "验证签名错误"),
    PARAM_DECRYPT_ERROR("12004", "参数解密异常"),
    ASK_RESPONSE_INVALID("12005", "http返回结果无效"),
    HANDLE_RESPONSE_CONTENT_ERROR("12006", "处理http结果异常"),

    /**
     * 2开头为服务端异常
     */
    ASK_SERVICE_ERROR("21000", "请求服务异常, 无法建立tcp"),
    ASK_SEND_TIMEOUT("21001", "http发送超时"),
    ASK_READ_TIMEOUT("21002", "网络异常，请核查"),
    CLOSE_RESPONSE_ERROR("21003", "关闭返回对象异常"),
    CLOSE_RESPONSE_CONTENT_ERROR("21004", "关闭返回流异常"),
    HTTP_NO_RESPONSE("21005", "http调用无返回"),
    HTTP_STATUS_ERROR("21006", "http返回码异常，请核查, %s"),
    PATTERN_GATEWAY_ERROR("21007", "调用第三方服务网关错误, %s");

    private String code;
    private String message;

    HttpErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
