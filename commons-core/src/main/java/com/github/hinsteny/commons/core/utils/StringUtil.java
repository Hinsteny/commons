package com.github.hinsteny.commons.core.utils;

import java.util.Collection;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 字符串相关操作方法工具类
 *
 * @author Hinsteny
 * @version DateUtil: DateUtil 2019-05-08 17:11 All rights reserved.$
 */
public class StringUtil {

    private static final Logger LOGGER = LogManager.getLogger(StringUtil.class);

    public static final String EMPTY = "";

    public static final char SPACE_CHAR = ' ';

    /**
     * 敏感信息脱敏默认替换字符
     */
    private static final Character CONCEAL_CHAR = '*';

    /**
     * <p>Replace sensitive substring with mark char to one string.</p>
     *
     * <pre>
     * StringUtils.replaceSensInfo(null, 5, 10, '*')      = ""
     * StringUtils.replaceSensInfo("123456", 3, 6, '*')   = "123***"
     * StringUtils.replaceSensInfo("123456", 3, 10, '*')  = "123***"
     * </pre>
     *
     * @param origin  the String to replace
     * @param start  the start index in origin
     * @param end  the end index in origin
     * @param shadow  the char use to be replace
     * @return result
     * @since 1.0
     */
    public static String replaceSensInfo(String origin, Integer start, Integer end, Character shadow) {
        char[] news = new char[0];
        int length;
        if (null != origin && (length = origin.length()) > 0) {
            if (start < 0 || start > length || end < 0 || end < start) {
                throw new RuntimeException("param illegal!");
            }
            end = (end > origin.length()) ? origin.length() : end;
            news = new char[end];
            origin.getChars(0, start, news, 0);
            shadow = (null == shadow) ? CONCEAL_CHAR : shadow;
            char[] x = repeatChar(new char[end], start, end, shadow);
            System.arraycopy(x, start, news, start, end - start);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Origin string: {}, news: {}", origin, String.valueOf(news));
        }
        return String.valueOf(news);
    }

    /**
     * 用指定字符填充字符数组
     * @param value 原始内容
     * @param srcBegin 开始位置
     * @param srcEnd 结束位置
     * @param repeat 填充字符
     * @return result
     */
    public static char[] repeatChar(char[] value, int srcBegin, int srcEnd, char repeat) {
        if (srcBegin < 0) {
            throw new StringIndexOutOfBoundsException(srcBegin);
        }
        if (srcEnd > value.length) {
            throw new StringIndexOutOfBoundsException(srcEnd);
        }
        if (srcBegin > srcEnd) {
            throw new StringIndexOutOfBoundsException(srcEnd - srcBegin);
        }
        for (int i = srcBegin, j = srcEnd; i < j; i++) {
            value[i] = repeat;
        }
        return value;
    }

    /**
     * 检测字符串是否为{@code null}或者为空（{@link #EMPTY}），当字符串为<code>null</code>或者为空时，返回<code>true</code>，
     * 否则返回<code>false</code>
     *
     * @param str 待检测的字符串
     * @return 当字符串为<code>null</code>或者为空时，返回<code>true</code>，否则返回<code>false</code>
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 检测字符串是否为空白，空白囊括如下三种情况：
     * <ol>
     * <li>null</li>
     * <li>空字符串（""）</li>
     * <li>全部为空白字符，包括制表符/空格/换行符等，具体参见：{@link Character#isWhitespace(char)}</li>
     * </ol>
     * 当一个字符串满足上面三个条件中的任何一个，即为空白。当字符串为空白时，返回{@code true}，否则返回{@code false}
     *
     * @param str 待检测的字符串
     * @return 当字符串为空白时，返回{@code true}，否则返回{@code false}
     */
    public static boolean isBlank(String str) {
        if (isNullOrEmpty(str)) {
            return true;
        }

        int strLength = str.length();
        int index = 0;
        while (index < strLength) {
            if (!Character.isWhitespace(str.charAt(index++))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串不为空
     * @param str 字符串
     * @return result
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 获取字符串的长度，若字符串为{@code null},字符串的长度为0
     *
     * @param str 需要获取长度的字符串
     * @return 返回字符串的长度，若字符串为{@code null},字符串的长度为0
     */
    public static int length(String str) {
        return str == null ? 0 : str.length();
    }

    /**
     * 移除字符串头部和尾部连续的空白字符。
     * @param str 需要操作的字符串
     * @return 返回移除移除收尾空白字符后的字符串
     */
    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    /**
     * 左侧填充字符。
     * <ul>
     * <li>如果原始字符串为{@code null}或者长度不小于指定扩充长度，则直接原样返回。</li>
     * <li>如果字符串长度小于指定的长度，则在左侧填充空格字符{@link #SPACE_CHAR}以达到指定长度{@code targetLength}。</li>
     * </ul>
     *
     * @param str 原始字符串
     * @param targetLength 需要填充到的目标长度
     * @return 左侧填充字符到指定长度的字符串
     */
    public static String leftPad(String str, int targetLength) {
        return leftPad(str, targetLength, SPACE_CHAR);
    }

    /**
     * 左侧填充字符。
     * <ul>
     * <li>如果原始字符串为{@code null}或者长度不小于指定扩充长度，则直接原样返回。</li>
     * <li>如果字符串长度小于指定的长度，则在左侧填充{@code padChar}以达到指定长度{@code targetLength}。</li>
     * </ul>
     *
     * @param str 原始字符串
     * @param targetLength 需要填充到的目标长度
     * @param padChar 填充时使用的字符
     * @return 左侧填充字符到指定长度的字符串
     */
    public static String leftPad(String str, int targetLength, char padChar) {
        if (str == null) {
            return null;
        }
        //如果字符串长度不小于目标长度，则直接返回
        if (str.length() >= targetLength) {
            return str;
        }

        return repeat(padChar, targetLength - str.length()) + str;
    }

    /**
     * 右侧填充字符。
     * <ul>
     * <li>如果原始字符串为{@code null}或者长度不小于指定扩充长度，则直接原样返回。</li>
     * <li>如果字符串长度小于指定的长度，则在右侧填充空格字符{@link #SPACE_CHAR}以达到指定长度{@code targetLength}。</li>
     * </ul>
     * @param str 原始字符串
     * @param targetLength 需要填充到的目标长度
     * @return 右侧填充字符到指定长度的字符串
     */
    public static String rightPad(String str, int targetLength) {
        return rightPad(str, targetLength, SPACE_CHAR);
    }

    /**
     * 右侧填充字符。
     * <ul>
     * <li>如果原始字符串为{@code null}或者长度不小于指定扩充长度，则直接原样返回。</li>
     * <li>如果字符串长度小于指定的长度，则在右侧填充{@code padChar}以达到指定长度{@code targetLength}。</li>
     * </ul>
     * @param str 原始字符串
     * @param targetLength 需要填充到的目标长度
     * @param padChar 填充时使用的字符
     * @return 右侧填充字符到指定长度的字符串
     */
    public static String rightPad(String str, int targetLength, char padChar) {
        if (str == null) {
            return null;
        }
        //如果字符串长度不小于目标长度，则直接返回
        if (str.length() >= targetLength) {
            return str;
        }

        return str + repeat(padChar, targetLength - str.length());
    }

    /**
     * 重复字符指定次数，若重复次数不大于0，则返回{@code null}.
     * <pre>
     *     StringUtils.repeat('A', -1) = null
     *     StringUtils.repeat('A', 0) = null
     *     StringUtils.repeat('A', 2) = "AA"
     * </pre>
     * @param c 需要重复的字符
     * @param repeat 需要重复的次数
     * @return 返回重复字符指定次数后的字符串
     */
    public static String repeat(char c, int repeat) {
        if (repeat <= 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder(repeat);
        for (int i = 0; i < repeat; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 重复字符串指定次数，若重复次数不大于0，则返回{@code null}.
     * <pre>
     *     StringUtils.repeat('abc', -1) = null
     *     StringUtils.repeat('abc', 0) = null
     *     StringUtils.repeat('abc', 2) = "abcabc"
     * </pre>
     * @param str 需要重复的字符串
     * @param repeat 需要重复的次数
     * @return 返回重复字符串指定次数后的字符串
     */
    public static String repeat(String str, int repeat) {
        if (str == null || repeat <= 0) {
            return null;
        }

        //只重复一次或者原字符串为空字符串时，直接返回原字符串即可
        if (repeat == 1 || str.length() == 0) {
            return str;
        }

        StringBuilder sb = new StringBuilder(repeat * str.length());
        for (int i = 0; i < repeat; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 拼接集合元素为字符串，使用{@code delimiter}参数作为分隔符。当集合为{@code null}，直接返回{@code null}。
     * 如果分隔符{@code delimiter}为{@code null}，则使用{@link #EMPTY}作为分隔符。
     * <pre>
     *     list.add("a");
     *     list.add("b");
     *     list.add("c");
     *     StringUtils.join(list, ",") = "a,b,c";
     * </pre>
     * @param <T> 集合类型
     * @param collection 需要拼接的集合
     * @param delimiter 元素之间拼接时的分隔符
     * @return 拼接后的字符串
     */
    public static <T> String join(Collection<T> collection, String delimiter) {
        if (collection == null) {
            return null;
        }

        if (delimiter == null) {
            delimiter = EMPTY;
        }
        Iterator<T> it = collection.iterator();
        StringBuilder sb = new StringBuilder();

        if (it.hasNext()) {
            sb.append(it.next());
        } else {
            return null;
        }

        while (it.hasNext()) {
            sb.append(delimiter).append(it.next());
        }
        return sb.toString();
    }

    /**
     * 拼接数组元素为字符串，使用{@code delimiter}参数作为分隔符。当数组为{@code null}或者无元素，直接返回{@code null}。
     * 如果分隔符{@code delimiter}为{@code null}，则使用{@link #EMPTY}作为分隔符。
     * <pre>
     *     String[] array = new String[]{"a", "b", "c"};
     *     StringUtils.join(array, ",") = "a,b,c";
     * </pre>
     * @param array 需要拼接的数组
     * @param delimiter 元素之间拼接时的分隔符
     * @param <T> 集合类型
     * @return 拼接后的字符串
     */
    public static <T> String join(T[] array, String delimiter) {
        if (array == null || array.length == 0) {
            return null;
        }

        if (delimiter == null) {
            delimiter = EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        //标识是否是拼接第一个元素
        boolean isFirst = true;
        for (T t : array) {
            if (!isFirst) {
                sb.append(delimiter);
            }
            sb.append(t);
            isFirst = false;
        }
        return sb.toString();
    }

    /**
     * 获取多个字符串最长的公共字符串前缀
     * @param strings 字符串数组
     * @return result
     */
    public static String getLongestCommonPrefix(String[] strings) {
        if (strings == null || strings.length == 0) {
            return null;
        }
        if (strings.length == 1) {
            return strings[0];
        }

        int currentCharIndex = 0;
        for (; currentCharIndex < strings[0].length(); currentCharIndex++) {
            for (int currentStrIndex = 1; currentStrIndex < strings.length; currentStrIndex++) {
                //如果到达字符串末尾或者找到不相等的字符串，则返回匹配
                if (currentCharIndex > strings[currentStrIndex].length()
                    || strings[0].charAt(currentCharIndex) != strings[currentStrIndex].charAt(currentCharIndex)) {
                    return strings[0].substring(0, currentCharIndex);
                }
            }
        }

        return strings[0].substring(0, currentCharIndex);
    }

    /**
     * byte数组转化为16进制字符串
     * @param bytes 原始数据
     * @return result
     */
    public static String byteToHexString(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            int source = (bytes[i] & 0xFF);
            String strHex = Integer.toHexString(source);
            sb.append(strHex.charAt(0));
        }
        return  sb.toString();
    }

}
