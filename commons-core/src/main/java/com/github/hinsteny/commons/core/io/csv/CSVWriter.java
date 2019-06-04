package com.github.hinsteny.commons.core.io.csv;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * CSV文件写入工具类
 *
 * @author Hinsteny
 * @version CSVWriter: CSVWriter 2019-05-10 09:54 All rights reserved.$
 */
public class CSVWriter {

    /**
     * 缓冲流读取
     */
    private BufferedWriter writer;

    /**
     * 构建csv writer
     *
     * @param stream csv文件输出流
     * @param charset 编码
     */
    public CSVWriter(OutputStream stream, Charset charset) {
        this.writer = new BufferedWriter(new OutputStreamWriter(stream, charset));
    }

    /**
     * 写入CSV行
     * @param csvLine 行内容
     * @throws IOException IO异常
     */
    public void write(WritableCSVLine csvLine) throws IOException {
        this.writer.write(csvLine.getFormattedTokensStr());
    }

    /**
     * 刷新缓冲区
     * @throws IOException IO异常
     */
    public void flush() throws IOException {
        this.writer.flush();
    }

    /**
     * 关闭写入
     * @throws IOException IO异常
     */
    public void close() throws IOException {
        this.writer.close();
    }

}
