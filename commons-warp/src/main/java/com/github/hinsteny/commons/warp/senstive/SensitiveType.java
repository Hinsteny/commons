package com.github.hinsteny.commons.warp.senstive;

/**
 * 敏感信息格式化类型.
 *
 * @author Hinsteny
 * @version SensitiveType: 2019-08-12 14:49 All rights reserved.$
 */
public enum SensitiveType {

    /**
     * 身份证
     */
    ID_CARD("", "", ""),

    /**
     * 手机号
     */
    PHONE("(\\d{3})\\d{4}(\\d{4})", "$1****$2", "手机号中间四位掩盖"),

    /**
     * 银行卡
     */
    BANK_CARD("", "", ""),

    /**
     * 姓名
     */
    NAME("", "", ""),

    /**
     * 身份证Base64
     */
    ID_CARD_PIC("", "", ""),
    ;

    SensitiveType(String regex, String replacement, String memo) {
        this.regex = regex;
        this.replacement = replacement;
        this.memo = memo;
    }

    /**
     *
     */
    private String regex;

    /**
     * 格式化模板
     */
    private String replacement;

    /**
     * 备注
     */
    private String memo;

    public String getRegex() {
        return regex;
    }

    public String getReplacement() {
        return replacement;
    }

    public String getMemo() {
        return memo;
    }

    @Override
    public String toString() {
        return "SensitiveType{" +
            "regex='" + regex + '\'' +
            ", replacement='" + replacement + '\'' +
            ", memo='" + memo + '\'' +
            "} " + super.toString();
    }
}
