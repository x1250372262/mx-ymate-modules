package com.mx.ymate.dev.util;


import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * @Author: mengxiang.
 * @Date 2025/04/24.
 * @Time: 11:00.
 * @Description: 时间工具类
 */
public class TimeUtil {


    private TimeUtil() {

    }

    /**
     * 本月第一天00:00:00
     *
     * @return yyyy-MM-dd
     */
    public static long monthStart() {
        return DateUtil.beginOfMonth(new Date()).getTime();
    }

    /**
     * 本月末23:59:59
     *
     * @return
     */
    public static long monthEnd() {
        return DateUtil.endOfMonth(new Date()).getTime();
    }


    /**
     * 本年第一天00:00:00
     *
     * @return yyyy-MM-dd
     */
    public static long yearStart() {
        return DateUtil.beginOfYear(new Date()).getTime();
    }

    /**
     * 本年末23:59:59
     *
     * @return
     */
    public static long yearEnd() {
        return DateUtil.endOfYear(new Date()).getTime();
    }

    /**
     * 本周第一天00:00:00
     *
     * @return yyyy-MM-dd
     */
    public static long weekStart() {
        return DateUtil.beginOfWeek(new Date()).getTime();
    }

    /**
     * 本周末23:59:59
     *
     * @return
     */
    public static long weekEnd() {
        return DateUtil.endOfWeek(new Date()).getTime();
    }

}
