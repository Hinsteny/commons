package com.github.hinsteny.commons.core.base;

/**
 * 时间段对象
 *
 * @author Hinsteny
 * @version Duration: Duration 2019-05-08 17:19 All rights reserved.$
 */
public class Duration {

    /** 一秒包含的纳秒数 */
    public static final int NANOS_PER_SECOND = 1000_000_000;

    /** 一小时包含的秒数 */
    public static final int SECONDS_PER_HOUR = 3600;

    /** 一天包含的秒数 */
    public static final int SECONDS_PER_DAY  = 24 * 3600;

    /** 时间段：秒及更大范围的部分 */
    private long            seconds;

    /** 时间段：毫秒及其更精确的部分 */
    private int             nanos;

    private Duration(long seconds, int nanos) {
        this.seconds = seconds;
        this.nanos = nanos;
    }

    /**
     * 根据毫秒构建时间段
     * @param millis 毫秒数
     * @return 根据毫秒构建的时间段对象
     */
    public static Duration ofMillis(long millis) {
        long secs = millis / 1000;
        int mos = (int) (millis % 1000);
        if (mos < 0) {
            mos += 1000;
            secs--;
        }
        return new Duration(secs, mos * 1000_000);
    }

    /**
     * 根据纳秒构建时间段
     * @param nanos 纳秒数
     * @return 根据纳秒构建的时间段对象
     */
    public static Duration ofNanos(long nanos) {
        long secs = nanos / NANOS_PER_SECOND;
        int nos = (int) (nanos % NANOS_PER_SECOND);
        if (nos < 0) {
            nos += NANOS_PER_SECOND;
            secs--;
        }
        return new Duration(secs, nos);
    }

    /**
     * 根据秒数构建时间段
     * @param seconds 秒数
     * @return 根据秒数构建的时间段对象
     */
    public static Duration ofSeconds(long seconds) {
        return new Duration(seconds, 0);
    }

    /**
     * 根据分钟数构建时间段
     * @param minutes 分钟数
     * @return 根据分钟数构建的时间段对象
     */
    public static Duration ofMinutes(long minutes) {
        return new Duration(minutes * 60, 0);
    }

    /**
     * 根据小时数构建时间段
     * @param hours 小时数
     * @return 根据小时数构建的时间段对象
     */
    public static Duration ofHours(long hours) {
        return new Duration(hours * SECONDS_PER_HOUR, 0);
    }

    /**
     * 根据天数构建时间段
     * @param days 天数
     * @return 根据天数构建的时间段对象
     */
    public static Duration ofDays(long days) {
        return new Duration(days * SECONDS_PER_DAY, 0);
    }

    /**
     * 获取时间段所代表的天数
     * @return 获取时间段所代表的天数
     */
    public long toDays() {
        return seconds / SECONDS_PER_DAY;
    }

    /**
     *
     * 获取时间段所代表的小时数
     * @return 获取时间段所代表的小时数
     */
    public long toHours() {
        return seconds / SECONDS_PER_HOUR;
    }

    /**
     * 获取时间段所代表的分钟数
     * @return 获取时间段所代表的分钟数
     */
    public long toMinutes() {
        return seconds / 60;
    }

    /**
     * 获取时间段所代表的秒数
     * @return 时间段所代表的秒数
     */
    public long toSeconds() {
        return seconds;
    }

    /**
     * 获取时间段所代表的毫秒数
     * @return 时间段所代表的毫秒数
     */
    public long toMillis() {
        long millis = multiplyExact(seconds, 1000);
        millis = addExact(millis, nanos / 1000_000);
        return millis;
    }

    /**
     * 获取时间段所代表的纳秒数
     * @return 时间段所代表的纳秒数
     */
    public long toNanos() {
        long totalNanos = multiplyExact(seconds, 1000_000_000);
        totalNanos = addExact(totalNanos, nanos);
        return totalNanos;
    }

    /**
     * 时间段是否为负。当为负时，返回{@code true},否则返回{@code false}。
     * @return 当时间段为负时，返回{@code true},否则返回{@code false}。
     */
    public boolean isNegative() {
        return seconds < 0;
    }

    /**
     * 两数相乘，当发生溢出时，抛出异常{@link ArithmeticException}
     * @param x 第一个数
     * @param y 第二个数
     * @return 两数的乘机
     */
    private long multiplyExact(long x, long y) {
        long r = x * y;
        long ax = Math.abs(x);
        long ay = Math.abs(y);
        if (((ax | ay) >>> 31 != 0)) {
            if (((y != 0) && (r / y != x)) || (x == Long.MIN_VALUE && y == -1)) {
                throw new ArithmeticException("long overflow");
            }
        }
        return r;
    }

    /**
     * 两数相加，当发生溢出时，抛出异常{@link ArithmeticException}
     * @param x 第一个数
     * @param y 第二个数
     * @return 返回两数之和
     */
    private long addExact(long x, long y) {
        long r = x + y;
        if (((x ^ r) & (y ^ r)) < 0) {
            throw new ArithmeticException("long overflow");
        }
        return r;
    }
}
