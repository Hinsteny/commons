package com.github.hinsteny.commons.core.io.csv;

/**
 * csv行模型
 *
 * @author Hinsteny
 * @version CSVLine: CSVLine 2019-05-10 10:11 All rights reserved.$
 */
public interface CSVLine {

    /**
     * 列分隔符
     */
    char COMMA = ',';

    /**
     * 转义符
     */
    char DOUBLE_QUOTE = '"';

    /**
     * 是否还有下一个token
     *
     * @return
     */
    boolean hasMore();

    /**
     * 返回下一个token
     *
     * @return
     */
    String nextToken();

    /**
     * 重置csv行状态
     * <ul>
     * <li>对于读模式状态，{@link #reset()}后可以从头开始读取</li>
     * <li>对于写模式状态，{@link #reset()}后会清空写入csv行中的数据，准备重新接收数据</li>
     * </ul>
     */
    void reset();
}
