package com.github.hinsteny.commons.core.io.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * csv 行读模型
 *
 * @author Hinsteny
 * @version ReadableCSVLine: ReadableCSVLine 2019-05-10 09:56 All rights reserved.$
 */
public class ReadableCSVLine implements CSVLine {

    /**
     * csv行原始记录
     */
    private String source;

    private int maxIndex;

    /**
     * 内容数组，包含当前行的所有列
     */
    private String[] tokens;

    /**
     * 当前处理到的索引值
     */
    private int currentIndex = 0;

    public ReadableCSVLine(String source) {
        this.source = source;
        this.maxIndex = source.length() - 1;
    }

    /**
     * 返回下一个字符串，如果没有则返回<code>null</code>
     * 注意获取下一个字符串会引起当前索引的变化
     *
     * @return 当前行中的下一列
     */
    @Override
    public String nextToken() {
        if (!hasMore()) {
            return null;
        }
        int nextCommaIndex = findNextComma();
        //下一个token开始的索引（包含）
        int tokenBeginIndex = -1,
            //下一个token结束索引（包含）
            tokenEndIndex = -1;
        if (nextCommaIndex != -1) {
            tokenBeginIndex = currentIndex;
            tokenEndIndex = nextCommaIndex - 1;
            currentIndex = nextCommaIndex + 1;

            //如果第一个单元格就为空
            if (nextCommaIndex == tokenBeginIndex) {
                return "";
            }
        }
        //最后一列，不为空
        else if (currentIndex <= maxIndex) {
            tokenBeginIndex = currentIndex;
            tokenEndIndex = maxIndex;
            currentIndex = maxIndex + 1;
        }
        //最后一列，为空
        else if (currentIndex > maxIndex) {
            currentIndex++;
            return "";
        }

        StringBuilder token = new StringBuilder();
        char preChar = COMMA;

        //如果开头是双引号，则忽略
        if (source.charAt(tokenBeginIndex) == DOUBLE_QUOTE) {
            tokenBeginIndex++;
        }

        //如果最后一位是双引号，则忽略
        if (source.charAt(tokenEndIndex) == DOUBLE_QUOTE) {
            tokenEndIndex--;
        }

        while (tokenBeginIndex <= tokenEndIndex) {
            char currentChar = source.charAt(tokenBeginIndex++);
            boolean skip = false;
            //如果是两个连续引号，则处理为一个引号
            if (currentChar == DOUBLE_QUOTE && preChar == DOUBLE_QUOTE) {
                skip = true;
            }
            preChar = currentChar;
            if (!skip) {
                token.append(currentChar);
            }
        }
        return token.toString();
    }

    /**
     * 返回所有的字符串数组
     *
     * @return 当前行的字符串数组
     */
    public String[] tokens() {
        if (this.tokens != null) {
            return this.tokens;
        }
        List<String> tokens = new ArrayList<String>();
        while (hasMore()) {
            tokens.add(nextToken());
        }
        return (this.tokens = tokens.toArray(new String[tokens.size()]));
    }

    /**
     * 是否还有下一个元素
     *
     * @return 有下一列是返回<code>true</code>，否则返回<code>false</code>
     */
    @Override
    public boolean hasMore() {
        //(currentIndex == maxIndex + 1 && source.charAt(maxIndex) == ',')用于判断最后一列为空的时候的场景
        return currentIndex <= maxIndex || (currentIndex == maxIndex + 1 && source.charAt(maxIndex) == ',');
    }

    /**
     * 重置
     */
    @Override
    public void reset() {
        this.currentIndex = 0;
    }

    /**
     * 查找下一个逗号分隔符的位置，若没有则返回-1
     * 注意查找分隔符不会引起当前索引的变化
     *
     * @return 返回下一个逗号的索引，若没有下一个逗号，返回-1
     */
    private int findNextComma() {
        //当前是否在双引号中
        boolean inQuote = false;
        int findIndex = currentIndex;
        while (findIndex <= maxIndex) {
            char currentChar = source.charAt(findIndex);
            if (!inQuote && COMMA == currentChar) {
                return findIndex;
            } else if (DOUBLE_QUOTE == currentChar) {
                inQuote = !inQuote;
            }
            findIndex++;
        }

        return -1;
    }

}
