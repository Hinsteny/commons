package com.github.hinsteny.commons.core.utils;

import com.github.hinsteny.commons.core.base.Duration;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 时间工具类
 *
 * @author Hinsteny
 * @version DateUtil: DateUtil 2019-05-08 17:11 All rights reserved.$
 */
public class DateUtil {

    /**
     * 格式化通配符: yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String LONG_TIME = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 格式化通配符: yyyy-MM-dd HH:mm:ss
     */
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化通配符: yyyyMMddHHmmss
     */
    public static final String SIMPLE_DATE_TIME = "yyyyMMddHHmmss";

    /**
     * 格式化通配符: HH:mm:ss
     */
    public static final String TIME = "HH:mm:ss";

    /**
     * 格式化通配符: HHmmss
     */
    public static final String SIMPLE_TIME = "HHmmss";

    /** 用于缓存时间格式化对象，由于{@link SimpleDateFormat}不是线程安全的，所以不能共享一个实例 */
    private static LinkedBlockingQueue<SimpleDateFormat> sdfs = new LinkedBlockingQueue<>(20);

    /**
     * 对时间加上指定的年数。当需要增加的年数为0，直接返回原时间对象。否则返回增加年数后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加年数的日期
     * @param years 需要增加的年数。可以小于0，表示减少指定年数。
     * @return 当需要增加的年数为0，直接返回原时间对象。否则返回增加年数后的时间。
     */
    public static Date plusYears(Date date, int years) {
        //TODO date非空检查
        if (years == 0) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.YEAR, years + calendar.get(Calendar.YEAR));
        return calendar.getTime();
    }

    /**
     * 对时间加上指定的月数。当需要增加的月数为0，直接返回原时间对象。否则返回增加月数后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加月数的日期
     * @param months 需要增加的月数。可以小于0，表示减少指定月数。
     * @return 当需要增加的月数为0，直接返回原时间对象。否则返回增加月数后的时间。
     */
    public static Date plusMonths(Date date, int months) {
        //TODO date非空检查
        if (months == 0) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, months + calendar.get(Calendar.MONTH));
        return calendar.getTime();
    }

    /**
     * 对时间加上指定的周数。当需要增加的周数为0，直接返回原时间对象。否则返回增加周数后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加周数的日期
     * @param weeks 需要增加的周数。可以小于0，表示减少指定周数。
     * @return 当需要增加的周数为0，直接返回原时间对象。否则返回增加周数后的时间。
     */
    public static Date plusWeeks(Date date, int weeks) {
        //TODO date非空检查
        if (weeks == 0) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.WEEK_OF_YEAR, weeks + calendar.get(Calendar.WEEK_OF_YEAR));
        return calendar.getTime();
    }

    /**
     * 对时间加上指定的天数。当需要增加的天数为0，直接返回原时间对象。否则返回增加天数后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加天数的日期
     * @param days 需要增加的天数。可以小于0，表示减少指定天数。
     * @return 当需要增加的天数为0，直接返回原时间对象。否则返回增加天数后的时间。
     */
    public static Date plusDays(Date date, int days) {
        //TODO date非空检查
        if (days == 0) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, days + calendar.get(Calendar.DAY_OF_YEAR));
        return calendar.getTime();
    }

    /**
     * 对时间加上指定的小时。当需要增加的小时为0，直接返回原时间对象。否则返回增加小时后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加小时的日期
     * @param hours 需要增加的小时。可以小于0，表示减少指定小时。
     * @return 当需要增加的小时为0，直接返回原时间对象。否则返回增加小时后的时间。
     */
    public static Date plusHours(Date date, int hours) {
        //TODO date非空检查
        if (hours == 0) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours + calendar.get(Calendar.HOUR_OF_DAY));
        return calendar.getTime();
    }

    /**
     * 获取两个时间对象间的时间差，返回时间段对象。若{@link Duration#isNegative()}返回{@code true}，则表示结束时间比
     * 开始时间小。
     * @param start 开始时间
     * @param end 结束时间
     * @return 返回开始时间到结束时间的时间段对象
     */
    public static Duration between(Date start, Date end) {
        //TODO stat end 非空校验
        return Duration.ofMillis(end.getTime() - start.getTime());
    }

    /**
     * 根据模式{@code pattern}格式化时间，{@code  pattern}详细说明参见{@link SimpleDateFormat}
     * <pre>
     *     Date date = "2017-07-27 10:00:00.123"
     *     DateUtils.format(date, "yyyy-MM-dd") = "2017-07-27"
     *     DateUtils.format(date, "HH:mm") = "10:00"
     *     DateUtils.format(date, "SSS") = "123"
     * </pre>
     * @param date 需要格式化的时间
     * @param pattern 格式化模式
     * @return 时间格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        //TODO date pattern 非空
        SimpleDateFormat sdf = getSimpleDateFormat();
        sdf.applyPattern(pattern);
        try {
            return sdf.format(date);
        } finally {
            //使用完毕后，将对象放回缓存供后续使用，队列满了导致放回失败也没关系
            sdfs.offer(sdf);
        }
    }

    /**
     * 将时间字符串解析为时间对象。只有将整个时间字符串解析完，才算解析成功。而{@link SimpleDateFormat#parse(String)}只要是解析了一部分，
     * 即使不解析完，也算成功。
     * @param dateStr 时间字符串
     * @param pattern 时间模式
     * @return 字符串解析后的时间对象
     * @throws ParseException 时间字符串未被完全解析，抛出异常{@link ParseException}
     */
    public static Date parse(String dateStr, String pattern) throws ParseException {
        //TODO dateStr pattern 非空
        SimpleDateFormat sdf = getSimpleDateFormat();
        sdf.applyPattern(pattern);
        try {
            ParsePosition pos = new ParsePosition(0);
            Date date = sdf.parse(dateStr, pos);
            if (pos.getIndex() != dateStr.length()) {
                throw new ParseException("Unable to parse date: " + dateStr + " with pattern: " + pattern, -1);
            }
            return date;
        } finally {
            //使用完毕后，将对象放回缓存供后续使用，队列满了导致放回失败也没关系
            sdfs.offer(sdf);
        }
    }

    /**
     * 获取时间格式化对象。首先从缓存队列中获取，若获取到则返回，否则新建一个时间格式化对象。
     * @return 时间格式化对象
     */
    private static SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat sdf = sdfs.poll();
        if (sdf == null) {
            sdf = new SimpleDateFormat();
        }
        return sdf;
    }

}
