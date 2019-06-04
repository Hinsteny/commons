package com.github.hinsteny.commons.core.utils;

import java.util.Arrays;
import com.github.hinsteny.commons.core.functional.page.PageCondition;

/**
 * @author Hinsteny
 * @version AssertUtil: AssertUtil 2019-05-08 16:58 All rights reserved.$
 */
public abstract class AssertUtil {

    /**
     * 断言分页对象是否合法
     *
     * @param pageCondition 需要检查的对象
     */
    public static void assertPageConditionValid(PageCondition pageCondition) {
        assertTrue(pageCondition != null && pageCondition.getPageNum() > 0 && pageCondition.getLimit() > 0,
            null);
    }

    /**
     * 断言分页对象是否合法
     *
     * @param pageCondition 需要检查的对象
     * @param message 断言失败描述
     */
    public static void assertPageConditionValid(PageCondition pageCondition, String message) {
        assertTrue(pageCondition != null && pageCondition.getPageNum() > 0 && pageCondition.getLimit() > 0, message);
    }

    /**
     * 断言字符串是否为空白，若为空白抛出异常{@link AssertionError}，具体参见{@link StringUtil#isBlank(String)}
     *
     * @param str 需要检查的对象
     */
    public static void assertStringNotBlank(String str) {
        assertTrue(!StringUtil.isBlank(str), null);
    }

    /**
     * 断言字符串是否为空白，若为空白抛出异常{@link AssertionError}，具体参见{@link StringUtil#isBlank(String)}
     *
     * @param str 需要检查的对象
     * @param message 断言失败描述
     */
    public static void assertStringNotBlank(String str, String message) {
        assertTrue(!StringUtil.isBlank(str), message);
    }

    /**
     * 断言数字是否为正数（大于0），若小于等于0，则抛出异常{@link AssertionError}
     */
    public static void assertPositiveNumber(Number number) {
        assertPositiveNumber(number, null);
    }

    /**
     * 断言数字是否为正数（大于0），若小于等于0，则抛出异常{@link AssertionError}
     */
    public static void assertPositiveNumber(Number number, String message) {
        if (number == null) {
            fail(message);
        }
        long value = number.longValue();
        assertTrue(value > 0 || (value == 0L && number.doubleValue() > 0), message);
    }

    /**
     * 断言数字是否为正整数，若小于等于0或不是整数，则抛出异常{@link AssertionError}。注意这里的Integer并不是Java中的Integer，代表的是整数
     */
    public static void assertPositiveIntegerNumber(Number number) {
        assertPositiveIntegerNumber(number, null);
    }

    /**
     * 断言数字是否为正整数，若小于等于0或不是整数，则抛出异常{@link AssertionError}。注意这里的Integer并不是Java中的Integer，代表的是整数
     */
    public static void assertPositiveIntegerNumber(Number number, String message) {
        if (number == null) {
            fail(message);
        }
        long longValue = number.longValue();
        double doubleValue = number.doubleValue();
        boolean isInteger = (doubleValue - longValue == 0);
        boolean isPositive = (longValue > 0 || (longValue == 0L && number.doubleValue() > 0));

        assertTrue(isInteger & isPositive, message);
    }

    /**
     * 断言数字是否为大于或等于0的整数，若小于0或不是整数，则抛出异常{@link AssertionError}。注意这里的Integer并不是Java中的Integer，代表的是整数
     */
    public static void assertIntegerNumberGeZero(Number number) {
        assertIntegerNumberGeZero(number, null);
    }

    /**
     * 断言数字是否为大于或等于0的整数，若小于0或不是整数，则抛出异常{@link AssertionError}。注意这里的Integer并不是Java中的Integer，代表的是整数
     */
    public static void assertIntegerNumberGeZero(Number number, String message) {
        if (number == null) {
            fail(message);
        }
        long longValue = number.longValue();
        double doubleValue = number.doubleValue();
        boolean isInteger = (doubleValue - longValue == 0);
        boolean isGeZero = (longValue >= 0);

        assertTrue(isInteger & isGeZero, message);
    }

    /**
     * 检查对象是否为{@code null}。若为{@code null}，抛出异常{@link AssertionError}
     *
     * @param obj 需要检查的对象
     */
    public static void assertNotNull(Object obj) {
        assertNotNull(obj, null);
    }

    /**
     * 检查对象是否为{@code null}。若为{@code null}，抛出异常{@link AssertionError}
     *
     * @param obj 需要检查的对象
     * @param message 断言失败描述
     */
    public static void assertNotNull(Object obj, String message) {
        assertTrue(obj != null, message);
    }

    /**
     * 检查值是否符合预期。若和预期值不等，则断言失败。
     *
     * @param actual 真实值
     * @param expect 预期值
     * @param message 断言失败描述
     */
    public static void assertEquals(Object actual, Object expect, String message) {
        message = formatEqualsMessage(actual, expect, message);
        assertTrue(equals(actual, expect), message);
    }

    /**
     * 检测byte数组内容是否相等
     *
     * @param actual 实际数组
     * @param expect 预期数组
     * @param message 断言失败描述，可为空
     */
    public static void assertArrayEquals(byte[] actual, byte[] expect, String message) {
        message = formatEqualsMessage(actual, expect, message);
        assertTrue(arrayEquals(actual, expect), message);
    }

    /**
     * 检测整型数组内容是否相等
     *
     * @param actual 实际数组
     * @param expect 预期数组
     * @param message 断言失败描述，可为空
     */
    public static void assertArrayEquals(int[] actual, int[] expect, String message) {
        message = formatEqualsMessage(actual, expect, message);
        assertTrue(arrayEquals(actual, expect), message);
    }

    /**
     * 检测short数组内容是否相等
     *
     * @param actual 实际数组
     * @param expect 预期数组
     * @param message 断言失败描述，可为空
     */
    public static void assertArrayEquals(short[] actual, short[] expect, String message) {
        message = formatEqualsMessage(actual, expect, message);
        assertTrue(arrayEquals(actual, expect), message);
    }

    /**
     * 检测float数组内容是否相等
     *
     * @param actual 实际数组
     * @param expect 预期数组
     * @param message 断言失败描述，可为空
     */
    public static void assertArrayEquals(float[] actual, float[] expect, String message) {
        message = formatEqualsMessage(actual, expect, message);
        assertTrue(arrayEquals(actual, expect), message);
    }

    /**
     * 检测double数组内容是否相等
     *
     * @param actual 实际数组
     * @param expect 预期数组
     * @param message 断言失败描述，可为空
     */
    public static void assertArrayEquals(double[] actual, double[] expect, String message) {
        message = formatEqualsMessage(actual, expect, message);
        assertTrue(arrayEquals(actual, expect), message);
    }

    /**
     * 检测long数组内容是否相等
     *
     * @param actual 实际数组
     * @param expect 预期数组
     * @param message 断言失败描述，可为空
     */
    public static void assertArrayEquals(long[] actual, long[] expect, String message) {
        message = formatEqualsMessage(actual, expect, message);
        assertTrue(arrayEquals(actual, expect), message);
    }

    /**
     * 检测对象数组内容是否相等
     *
     * @param actual 实际数组
     * @param expect 预期数组
     * @param message 断言失败描述，可为空
     */
    public static void assertArrayEquals(Object[] actual, Object[] expect, String message) {
        message = formatEqualsMessage(actual, expect, message);
        assertTrue(arrayEquals(actual, expect), message);
    }

    /**
     * 检查值是否和预期不一致。若和预期值相等（== 或equals），则断言失败。
     *
     * @param actual 真实值
     * @param expect 预期值
     * @param message 断言失败描述
     */
    public static void assertNotEquals(Object actual, Object expect, String message) {
        assertTrue(!equals(actual, expect), message);
    }

    /**
     * 检查对象是否为{@code null}。若不为{@code null}，抛出异常{@link AssertionError}
     *
     * @param obj 需要检查的对象
     * @param message 断言失败描述
     */
    public static void assertNull(Object obj, String message) {
        assertTrue(obj == null, message);
    }

    /**
     * 检查条件是否为{@code true}
     *
     * @param condition 需要检查的条件
     * @param message 当条件为假时，需要抛出的异常描述，可为空
     */
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            fail(message);
        }
    }

    /**
     * 检查条件是否为{@code false}
     *
     * @param condition 需要检查的条件
     * @param message 当条件为真时，需要抛出的异常描述，可为空
     */
    public static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }

    /**
     * 断言失败
     *
     * @param message 断言失败的描述，可为空。
     */
    private static void fail(String message) {
        throw new AssertionError(message);
    }

    /**
     * 格式化字符串
     *
     * @param str 模板字符串
     * @param objs 格式化参数
     * @return 格式化后的字符串
     */
    private static String format(String str, Object... objs) {
        Object[] objsCopy;
        if (objs != null) {
            objsCopy = new Object[objs.length];
            for (int index = objs.length - 1; index >= 0; index--) {
                if (objs[index].getClass().isArray()) {
                    objsCopy[index] = Arrays.toString((Object[]) objs[index]);
                } else {
                    objsCopy[index] = objs[index];
                }
            }
        }
        return String.format(str, objs);
    }

    /**
     * 判断两个对象是否相等，包含三种情况：
     * <ol>
     * <li>actual和except都为{@code null}</li>
     * <li>actual == except</li>
     * <li>actual和except值等</li>
     * </ol>
     */
    private static boolean equals(Object actual, Object expect) {
        if (actual == null) {
            return expect == null;
        }
        return actual == expect || actual.equals(expect);
    }

    /**
     * 判断两个数组是否内容相等
     *
     * @param actual 数组实际值
     * @param except 数组预期值
     * @return 数组内容相等时，返回{@code true}
     */
    private static boolean arrayEquals(Object actual, Object except) {
        return Arrays.deepEquals(new Object[]{actual}, new Object[]{except});
    }

    /**
     * 格式化相等断言的描述
     *
     * @param actual 真实值
     * @param expect 预期值
     * @param message 断言失败描述
     * @return 返回格式化后的描述
     */
    private static String formatEqualsMessage(Object actual, Object expect, String message) {
        if (message != null) {
            return message;
        }
        return format("Except %s, Actual %s", expect, actual);
    }
}
