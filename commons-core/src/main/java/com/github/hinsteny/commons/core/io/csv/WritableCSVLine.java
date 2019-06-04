package com.github.hinsteny.commons.core.io.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * csv 行写模型
 *
 * @author Hinsteny
 * @version WritableCSVLine: WritableCSVLine 2019-05-10 09:55 All rights reserved.$
 */
public class WritableCSVLine implements CSVLine {

    /**
     * 内容数组，包含当前行的所有列
     */
    private List<String> tokens = new ArrayList<String>();

    /**
     * 当前处理到的索引值
     */
    private int currentIndex = 0;

    private StringBuffer formattedTokensStr = new StringBuffer();


    /**
     * 增加一个token
     *
     * @param token 单元
     */
    public void appendToken(Object token) {
        if (formattedTokensStr.length() > 0) {
            formattedTokensStr.append(COMMA);
        }
        formattedTokensStr.append(formatForPersistence(token.toString()));
    }

    /**
     * 返回下一个字符串，如果没有则返回<code>null</code>
     * 注意获取下一个字符串会引起当前索引的变化
     *
     * @return 当前行中的下一列
     */
    @Override
    public String nextToken() {
        return tokens.get(currentIndex++);
    }


    /**
     * 是否还有下一个元素
     *
     * @return 有下一列是返回<code>true</code>，否则返回<code>false</code>
     */
    @Override
    public boolean hasMore() {
        return currentIndex < tokens.size();
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        this.currentIndex = 0;
        this.formattedTokensStr = new StringBuffer();
        tokens.clear();
    }

    /**
     * 返回csv行对应的可写字符串
     *
     * @return 内容
     */
    public String getFormattedTokensStr() {
        return formattedTokensStr.toString();
    }


    /**
     * 格式化token为可写的格式
     *
     * @param token 单元
     * @return 内容
     */
    private String formatForPersistence(String token) {
        String formattedToken = token;
        //有双引号
        if (formattedToken.indexOf("\"") != -1) {
            formattedToken = formattedToken.replaceAll("\"", "\"\"");
        }

        //包含逗号
        if (formattedToken.indexOf(Character.toString(COMMA)) != -1) {
            formattedToken = DOUBLE_QUOTE + formattedToken + DOUBLE_QUOTE;
        }
        return formattedToken;
    }
}
