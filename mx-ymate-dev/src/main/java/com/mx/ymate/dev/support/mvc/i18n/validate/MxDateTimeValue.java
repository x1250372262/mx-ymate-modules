package com.mx.ymate.dev.support.mvc.i18n.validate;

import net.ymate.platform.commons.DateTimeHelper;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.validation.ValidateContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description:
 */
public class MxDateTimeValue implements Serializable {

    public static final String TODAY = "today";

    public static final String YESTERDAY = "yesterday";

    public static final String WEEK = "week";

    public static final String MONTH = "month";

    public static final String YEAR = "year";

    public static final int DATETIME_PART_MAX_LENGTH = 2;

    private final Date startDate;

    private Date endDate;

    private boolean single;

    public static MxDateTimeValue get(String paramName) {
        return (MxDateTimeValue) ValidateContext.getLocalAttributes().get(paramName);
    }

    public static MxDateTimeValue get(String paramName, IValueProcessor valueProcessor) {
        MxDateTimeValue dateTimeValue = get(paramName);
        if (dateTimeValue != null) {
            valueProcessor.process(dateTimeValue);
        }
        return dateTimeValue;
    }

    /**
     * @since 2.1.3
     */
    public static Long getStartDateTimeMillisOrNull(String paramName) {
        MxDateTimeValue dateTimeValue = get(paramName);
        if (dateTimeValue != null) {
            return dateTimeValue.getStartDateTimeMillisOrNull();
        }
        return null;
    }

    public static MxDateTimeValue parse(String dateTimeStr, boolean single) {
        return parse(dateTimeStr, null, null, single);
    }

    public static MxDateTimeValue parse(String dateTimeStr, String pattern, boolean single) {
        return parse(dateTimeStr, pattern, null, single);
    }

    public static MxDateTimeValue parse(String dateTimeStr, String pattern, String separator, boolean single) {
        MxDateTimeValue dateTimeValue = null;
        if (single) {
            Date date;
            if (StringUtils.equalsIgnoreCase(dateTimeStr, TODAY)) {
                date = DateTimeHelper.now().toDayStart().time();
            } else if (StringUtils.equalsIgnoreCase(dateTimeStr, YESTERDAY)) {
                date = DateTimeHelper.now().toDayStart().daysAdd(-1).time();
            } else if (StringUtils.equalsIgnoreCase(dateTimeStr, WEEK)) {
                date = DateTimeHelper.now().toDayStart().toWeekStart().time();
            } else if (StringUtils.equalsIgnoreCase(dateTimeStr, MONTH)) {
                date = DateTimeHelper.now().toDayStart().day(1).time();
            } else if (StringUtils.equalsIgnoreCase(dateTimeStr, YEAR)) {
                date = DateTimeHelper.now().toDayStart().month(1).day(1).time();
            } else {
                date = MxDateTimeValidator.parseDate(dateTimeStr, pattern);
            }
            if (date != null) {
                dateTimeValue = new MxDateTimeValue(date);
            }
        } else {
            if (StringUtils.equalsIgnoreCase(dateTimeStr, TODAY)) {
                dateTimeValue = today();
            } else if (StringUtils.equalsIgnoreCase(dateTimeStr, YESTERDAY)) {
                dateTimeValue = yesterday();
            } else if (StringUtils.equalsIgnoreCase(dateTimeStr, WEEK)) {
                dateTimeValue = week();
            } else if (StringUtils.equalsIgnoreCase(dateTimeStr, MONTH)) {
                dateTimeValue = month();
            } else if (StringUtils.equalsIgnoreCase(dateTimeStr, YEAR)) {
                dateTimeValue = year();
            } else {
                String[] dateTimeArr = StringUtils.split(dateTimeStr, StringUtils.defaultIfBlank(separator, "/"));
                if (ArrayUtils.isNotEmpty(dateTimeArr)) {
                    if (dateTimeArr.length <= DATETIME_PART_MAX_LENGTH) {
                        Date dateTimeBegin = MxDateTimeValidator.parseDate(dateTimeArr[0], pattern);
                        Date dateTimeEnd = null;
                        if (dateTimeBegin != null) {
                            if (dateTimeArr.length > 1 && !StringUtils.equalsIgnoreCase(StringUtils.trim(dateTimeArr[0]), StringUtils.trim(dateTimeArr[1]))) {
                                dateTimeEnd = MxDateTimeValidator.parseDate(dateTimeArr[1], pattern);
                            }
                            if (dateTimeEnd == null) {
                                dateTimeEnd = DateTimeHelper.bind(dateTimeBegin).toDayEnd().time();
                            }
                            dateTimeValue = new MxDateTimeValue(dateTimeBegin, dateTimeEnd);
                        }
                    }
                }
            }
        }
        return dateTimeValue;
    }

    /**
     * @return 返回当前时刻的日期时间值对象
     * @since 2.1.2
     */
    public static MxDateTimeValue now() {
        return new MxDateTimeValue(DateTimeHelper.now().time());
    }

    /**
     * @return 返回从今天零点到当前时刻的日期时间值对象
     * @since 2.1.2
     */
    public static MxDateTimeValue today() {
        return new MxDateTimeValue(DateTimeHelper.now().toDayStart().time(), DateTimeHelper.now().time());
    }

    /**
     * @return 返回从昨天零点到昨天23点59分59秒的时间值对象
     * @since 2.1.3
     */
    public static MxDateTimeValue yesterday() {
        DateTimeHelper yesterdayHelper = DateTimeHelper.now().toDayStart().daysAdd(-1);
        return new MxDateTimeValue(yesterdayHelper.time(), yesterdayHelper.toDayEnd().time());
    }

    /**
     * @return 返回从本周一零点到今天当前时刻的日期时间值对象
     * @since 2.1.2
     */
    public static MxDateTimeValue week() {
        return new MxDateTimeValue(DateTimeHelper.now().toWeekStart().time(), DateTimeHelper.now().time());
    }

    /**
     * @return 返回本月一号零点到今天当前时刻的日期时间值对象
     * @since 2.1.2
     */
    public static MxDateTimeValue month() {
        return new MxDateTimeValue(DateTimeHelper.now().day(1).toDayStart().time(), DateTimeHelper.now().time());
    }

    /**
     * @return 返回本年一月一号零点到今天当前时刻的日期时间值对象
     * @since 2.1.3
     */
    public static MxDateTimeValue year() {
        return new MxDateTimeValue(DateTimeHelper.now().month(1).day(1).toDayStart().time(), DateTimeHelper.now().time());
    }

    /**
     * @since 2.1.3
     */
    public MxDateTimeValue(long startDate) {
        this(DateTimeHelper.bind(startDate).time());
    }

    /**
     * @since 2.1.3
     */
    public MxDateTimeValue(long startDate, long endDate) {
        this(DateTimeHelper.bind(startDate).time(), DateTimeHelper.bind(endDate).time());
    }

    public MxDateTimeValue(Date startDate) {
        this.startDate = startDate;
        this.single = true;
    }

    public MxDateTimeValue(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public boolean isSingle() {
        return single;
    }

    public boolean isNullStartDate() {
        return startDate == null;
    }

    public boolean isNullEndDate() {
        return endDate == null;
    }

    /**
     * @return 获取开始日期毫秒值，若为空则返回0
     */
    public long getStartDateTimeMillis() {
        return isNullStartDate() ? 0 : startDate.getTime();
    }

    public Long getStartDateTimeMillisOrNull() {
        if (isNullStartDate()) {
            return null;
        }
        return startDate.getTime();
    }

    public Timestamp getStartDateTimestampOrNull() {
        if (isNullStartDate()) {
            return null;
        }
        return new Timestamp(startDate.getTime());
    }

    /**
     * @return 获取结束日期毫秒值，若为空则返回0
     */
    public long getEndDateTimeMillis() {
        return isNullEndDate() ? 0 : endDate.getTime();
    }

    public Long getEndDateTimeMillisOrNull() {
        if (isNullEndDate()) {
            return null;
        }
        return endDate.getTime();
    }

    public Timestamp getEndDateTimestampOrNull() {
        if (isNullEndDate()) {
            return null;
        }
        return new Timestamp(endDate.getTime());
    }

    public DateTimeHelper bindStartDate() {
        return DateTimeHelper.bind(startDate);
    }

    public DateTimeHelper bindEndDate() {
        return DateTimeHelper.bind(endDate);
    }

    /**
     * @return 计算两日期之间相差天数
     * @since 2.1.2
     */
    public long getMaxDays() {
        return getMaxTimeMillis() / DateTimeUtils.DAY;
    }

    /**
     * @return 计算两日期之间相差毫秒值
     * @since 2.1.2
     */
    public long getMaxTimeMillis() {
        if (isNullStartDate() || isNullEndDate()) {
            return 0L;
        }
        return Math.abs(DateTimeHelper.bind(startDate).subtract(endDate));
    }

    /**
     * @return 以字符串输出
     * @since 2.1.2
     */
    @Override
    public String toString() {
        return toString(DateTimeUtils.YYYY_MM_DD_HH_MM_SS, null);
    }

    /**
     * @param dateFormat 日期格式字符串
     * @param separator  时间段字符串之间的分割符号
     * @return 以字符串输出
     * @since 2.1.2
     */
    public String toString(String dateFormat, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        if (startDate != null) {
            stringBuilder.append(DateTimeHelper.bind(startDate).toString(dateFormat));
        }
        if (endDate != null) {
            stringBuilder.append(StringUtils.SPACE)
                    .append(StringUtils.defaultIfBlank(separator, "/"))
                    .append(StringUtils.SPACE)
                    .append(DateTimeHelper.bind(endDate).toString(dateFormat));
        }
        return stringBuilder.toString();
    }

    public interface IValueProcessor {

        /**
         * 处理日期时间值
         *
         * @param dateTimeValue 日期时间值对象
         */
        void process(MxDateTimeValue dateTimeValue);
    }
}
