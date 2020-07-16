package com.github.hinsteny.commons.core.utils;

import static java.util.Calendar.MILLISECOND;

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
     * 格式化通配符: yyyyMMdd
     */
    public static final String DATE = "yyyy-MM-dd";

    /**
     * 格式化通配符: yyyyMMdd
     */
    public static final String SIMPLE_DATE = "yyyyMMdd";

    /**
     * 格式化通配符: HH:mm:ss
     */
    public static final String TIME = "HH:mm:ss";

    /**
     * 格式化通配符: HHmmss
     */
    public static final String SIMPLE_TIME = "HHmmss";

    /**
     * This is half a month, so this represents whether a date is in the top
     * or bottom half of the month.
     */
    public static final int SEMI_MONTH = 1001;

    /**
     * 用于缓存时间格式化对象，由于{@link SimpleDateFormat}不是线程安全的，所以不能共享一个实例
     */
    private static LinkedBlockingQueue<SimpleDateFormat> sdfs = new LinkedBlockingQueue<>(20);

    private static final int[][] fields = {
        {Calendar.MILLISECOND},
        {Calendar.SECOND},
        {Calendar.MINUTE},
        {Calendar.HOUR_OF_DAY, Calendar.HOUR},
        {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM
            /* Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH */
        },
        {Calendar.MONTH, SEMI_MONTH},
        {Calendar.YEAR},
        {Calendar.ERA}};

    /**
     * Calendar modification types.
     */
    private enum ModifyType {
        /**
         * Truncation.
         */
        TRUNCATE,

        /**
         * Rounding.
         */
        ROUND,

        /**
         * Ceiling.
         */
        CEILING
    }

    //-----------------------------------------------------------------------
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

    //-----------------------------------------------------------------------
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

    //-----------------------------------------------------------------------
    /**
     * 对时间加上指定的年数。当需要增加的年数为0，直接返回原时间对象。否则返回增加年数后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加年数的日期
     * @param years 需要增加的年数。可以小于0，表示减少指定年数。
     * @return 当需要增加的年数为0，直接返回原时间对象。否则返回增加年数后的时间。
     */
    public static Date addYears(Date date, int years) {
        return add(date, Calendar.YEAR, years);
    }

    //-----------------------------------------------------------------------
    /**
     * 对时间加上指定的月数。当需要增加的月数为0，直接返回原时间对象。否则返回增加月数后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加月数的日期
     * @param months 需要增加的月数。可以小于0，表示减少指定月数。
     * @return 当需要增加的月数为0，直接返回原时间对象。否则返回增加月数后的时间。
     */
    public static Date addMonths(Date date, int months) {
        return add(date, Calendar.MONTH, months);
    }

    //-----------------------------------------------------------------------
    /**
     * 对时间加上指定的周数。当需要增加的周数为0，直接返回原时间对象。否则返回增加周数后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加周数的日期
     * @param weeks 需要增加的周数。可以小于0，表示减少指定周数。
     * @return 当需要增加的周数为0，直接返回原时间对象。否则返回增加周数后的时间。
     */
    public static Date addWeeks(Date date, int weeks) {
        return add(date, Calendar.WEEK_OF_YEAR, weeks);
    }

    //-----------------------------------------------------------------------
    /**
     * 对时间加上指定的天数。当需要增加的天数为0，直接返回原时间对象。否则返回增加天数后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加天数的日期
     * @param days 需要增加的天数。可以小于0，表示减少指定天数。
     * @return 当需要增加的天数为0，直接返回原时间对象。否则返回增加天数后的时间。
     */
    public static Date addDays(Date date, int days) {
        return add(date, Calendar.DAY_OF_MONTH, days);
    }

    //-----------------------------------------------------------------------
    /**
     * 对时间加上指定的小时。当需要增加的小时为0，直接返回原时间对象。否则返回增加小时后的时间，此时为新时间对象，非原始时间对象。
     * @param date 需要增加小时的日期
     * @param hours 需要增加的小时。可以小于0，表示减少指定小时。
     * @return 当需要增加的小时为0，直接返回原时间对象。否则返回增加小时后的时间。
     */
    public static Date addHours(Date date, int hours) {
        return add(date, Calendar.HOUR_OF_DAY, hours);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of minutes to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date  the date, not null
     * @param minute  the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addMinutes(final Date date, final int minute) {
        return add(date, Calendar.MINUTE, minute);
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of seconds to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    public static Date addSeconds(final Date date, final int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    /**
     * 获取两个时间对象间的时间差，返回时间段对象。若{@link Duration#isNegative()}返回{@code true}，则表示结束时间比
     * 开始时间小。
     * @param start 开始时间
     * @param end 结束时间
     * @return 返回开始时间到结束时间的时间段对象
     */
    public static Duration between(Date start, Date end) {
        validateDateNotNull(start);
        validateDateNotNull(end);
        return Duration.ofMillis(end.getTime() - start.getTime());
    }

    //-----------------------------------------------------------------------
    /**
     * 判断两个时间是否处于同一个时间单位周期内.
     *
     * @param one time-one
     * @param two time-two
     * @param field 时间单位(秒, 分, 时, 天) {@link java.util.Calendar#MINUTE}
     * @return
     */
    public static boolean isTimeInSameTimeField(Date one, Date two, int field) {
        validateDateNotNull(one);
        validateDateNotNull(two);
        // Note: WEEK_YEAR can't be set with this method.
        if (field < 0 || field >= MILLISECOND) {
            throw new IllegalArgumentException("field is invalid");
        }
        final Date truncatedDate1 = truncate(one, field);
        final Date truncatedDate2 = truncate(two, field);
        return truncatedDate1.compareTo(truncatedDate2) == 0;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Rounds a date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the date-time of 28 Mar 2002
     * 13:45:01.231, if this was passed with HOUR, it would return
     * 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it
     * would return 1 April 2002 0:00:00.000.</p>
     *
     * <p>For a date in a timezone that handles the change to daylight
     * saving time, rounding to Calendar.HOUR_OF_DAY will behave as follows.
     * Suppose daylight saving time begins at 02:00 on March 30. Rounding a
     * date that crosses this time would produce the following values:
     * </p>
     * <ul>
     * <li>March 30, 2003 01:29 rounds to March 30, 2003 01:00</li>
     * <li>March 30, 2003 01:31 rounds to March 30, 2003 02:00</li>
     * <li>March 30, 2003 02:10 rounds to March 30, 2003 02:00</li>
     * <li>March 30, 2003 02:40 rounds to March 30, 2003 03:00</li>
     * </ul>
     *
     * @param date  the date to work with, not null
     * @param field  the field from {@code Calendar} or {@code SEMI_MONTH}
     * @return the different rounded date, not null
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date round(final Date date, final int field) {
        validateDateNotNull(date);
        final Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field, ModifyType.ROUND);
        return gval.getTime();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Truncates a date, leaving the field specified as the most
     * significant field.</p>
     *
     * <p>For example, if you had the date-time of 28 Mar 2002
     * 13:45:01.231, if you passed with HOUR, it would return 28 Mar
     * 2002 13:00:00.000.  If this was passed with MONTH, it would
     * return 1 Mar 2002 0:00:00.000.</p>
     *
     * @param date  the date to work with, not null
     * @param field  the field from {@code Calendar} or <code>SEMI_MONTH</code>
     * @return the different truncated date, not null
     * @throws IllegalArgumentException if the date is <code>null</code>
     * @throws ArithmeticException if the year is over 280 million
     */
    public static Date truncate(final Date date, final int field) {
        validateDateNotNull(date);
        final Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field, ModifyType.TRUNCATE);
        return gval.getTime();
    }

    //-----------------------------------------------------------------------
    /**
     * get the time period include startTime and endTime.
     *
     * @param date  the date, not null
     * @param calendarField  the {@code Calendar} field that the time period
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date[] getTimePeriod(final Date date, final int calendarField) {
        validateDateNotNull(date);
        Date[] period = new Date[2];
        period[0] = truncate(date, calendarField);
        Date median = add(period[0], calendarField, 1);
        period[1] = new Date(median.getTime() - 1);
        return period;
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the specified field to a date returning a new object.
     * This does not use a lenient calendar.
     * The original {@code Date} is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the {@code Calendar} field to set the amount to
     * @param amount the amount to set
     * @return a new {@code Date} set with the specified value
     * @throws IllegalArgumentException if the date is null
     */
    public static Date set(final Date date, final int calendarField, final int amount) {
        validateDateNotNull(date);
        // getInstance() returns a new object, so this method is thread safe.
        final Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.set(calendarField, amount);
        return c.getTime();
    }

    //-----------------------------------------------------------------------
    /**
     * Adds to a date returning a new object.
     * The original {@code Date} is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the calendar field to add to
     * @param amount  the amount to add, may be negative
     * @return the new {@code Date} with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    private static Date add(final Date date, final int calendarField, final int amount) {
        validateDateNotNull(date);
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Internal calculation method.</p>
     *
     * @param val  the calendar, not null
     * @param field  the field constant
     * @param modType  type to truncate, round or ceiling
     * @throws ArithmeticException if the year is over 280 million
     */
    private static void modify(final Calendar val, final int field, final ModifyType modType) {
        if (val.get(Calendar.YEAR) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }

        if (field == Calendar.MILLISECOND) {
            return;
        }

        // ----------------- Fix for LANG-59 ---------------------- START ---------------
        // see http://issues.apache.org/jira/browse/LANG-59
        //
        // Manually truncate milliseconds, seconds and minutes, rather than using
        // Calendar methods.

        final Date date = val.getTime();
        long time = date.getTime();
        boolean done = false;

        // truncate milliseconds
        final int millisecs = val.get(Calendar.MILLISECOND);
        if (ModifyType.TRUNCATE == modType || millisecs < 500) {
            time = time - millisecs;
        }
        if (field == Calendar.SECOND) {
            done = true;
        }

        // truncate seconds
        final int seconds = val.get(Calendar.SECOND);
        if (!done && (ModifyType.TRUNCATE == modType || seconds < 30)) {
            time = time - (seconds * 1000L);
        }
        if (field == Calendar.MINUTE) {
            done = true;
        }

        // truncate minutes
        final int minutes = val.get(Calendar.MINUTE);
        if (!done && (ModifyType.TRUNCATE == modType || minutes < 30)) {
            time = time - (minutes * 60000L);
        }

        // reset time
        if (date.getTime() != time) {
            date.setTime(time);
            val.setTime(date);
        }
        // ----------------- Fix for LANG-59 ----------------------- END ----------------

        boolean roundUp = false;
        for (final int[] aField : fields) {
            for (final int element : aField) {
                if (element == field) {
                    //This is our field... we stop looping
                    if (modType == ModifyType.CEILING || modType == ModifyType.ROUND && roundUp) {
                        if (field == SEMI_MONTH) {
                            //This is a special case that's hard to generalize
                            //If the date is 1, we round up to 16, otherwise
                            //  we subtract 15 days and add 1 month
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                            // ----------------- Fix for LANG-440 ---------------------- START ---------------
                        } else if (field == Calendar.AM_PM) {
                            // This is a special case
                            // If the time is 0, we round up to 12, otherwise
                            //  we subtract 12 hours and add 1 day
                            if (val.get(Calendar.HOUR_OF_DAY) == 0) {
                                val.add(Calendar.HOUR_OF_DAY, 12);
                            } else {
                                val.add(Calendar.HOUR_OF_DAY, -12);
                                val.add(Calendar.DATE, 1);
                            }
                            // ----------------- Fix for LANG-440 ---------------------- END ---------------
                        } else {
                            //We need at add one to this field since the
                            //  last number causes us to round up
                            val.add(aField[0], 1);
                        }
                    }
                    return;
                }
            }
            //We have various fields that are not easy roundings
            int offset = 0;
            boolean offsetSet = false;
            //These are special types of fields that require different rounding rules
            switch (field) {
                case SEMI_MONTH:
                    if (aField[0] == Calendar.DATE) {
                        //If we're going to drop the DATE field's value,
                        //  we want to do this our own way.
                        //We need to subtrace 1 since the date has a minimum of 1
                        offset = val.get(Calendar.DATE) - 1;
                        //If we're above 15 days adjustment, that means we're in the
                        //  bottom half of the month and should stay accordingly.
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        //Record whether we're in the top or bottom half of that range
                        roundUp = offset > 7;
                        offsetSet = true;
                    }
                    break;
                case Calendar.AM_PM:
                    if (aField[0] == Calendar.HOUR_OF_DAY) {
                        //If we're going to drop the HOUR field's value,
                        //  we want to do this our own way.
                        offset = val.get(Calendar.HOUR_OF_DAY);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        roundUp = offset >= 6;
                        offsetSet = true;
                    }
                    break;
                default:
                    break;
            }
            if (!offsetSet) {
                final int min = val.getActualMinimum(aField[0]);
                final int max = val.getActualMaximum(aField[0]);
                //Calculate the offset from the minimum allowed value
                offset = val.get(aField[0]) - min;
                //Set roundUp if this is more than half way between the minimum and maximum
                roundUp = offset > ((max - min) / 2);
            }
            //We need to remove this field
            if (offset != 0) {
                val.set(aField[0], val.get(aField[0]) - offset);
            }
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");
    }

    //-----------------------------------------------------------------------
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

    private static void validateDateNotNull(final Date date) {
        if (null == date) {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

}
