package com.claris.generatingcode.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */
public class OfTime {

    //默认使用系统当前时区
    private static final ZoneId ZONE = ZoneId.systemDefault();

    private final static SimpleDateFormat SDF_SHORT_TIME = new SimpleDateFormat("yyyyMMddHHmmss");

    private final static SimpleDateFormat SDF_LONG_TIME = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private final static SimpleDateFormat DATE_ALL = new SimpleDateFormat("yyyy-MM-dd");

    private final static SimpleDateFormat DATE_TIME_ALL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static SimpleDateFormat DATE_TIME_MINUTE = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private final static SimpleDateFormat DEFAULT_TIME = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);


    /**
     * 按照yyyy-MM-dd HH:mm:的格式字符串
     *
     * @param date
     * @return
     */
    public static String strDateAndM(String date) {
        if (Tools.notEmpty(date)) {
            try {
                Date forDate = DATE_TIME_ALL.parse(date);
                return date2Str(forDate, "yyyy-MM-dd HH:mm");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 将指定的时间按指定的格式处理
     *
     * @param date
     * @param format
     * @return
     */
    public static Date fomatDate(String date, String format) {
        DateFormat fmt = new SimpleDateFormat(format);
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 时间相减得到分钟数
     *
     * @param beginDateStr
     * @param endDateStr
     * @return
     */
    public static long getMinuteSub(String beginDateStr, String endDateStr) {
        long day = 0;
        Date beginDate = null, endDate = null;
        try {
            beginDate = DATE_TIME_MINUTE.parse(beginDateStr);
            endDate = DATE_TIME_MINUTE.parse(endDateStr);
            if (beginDate.getTime() == endDate.getTime()) {
                day = 1;
            } else {
                day = (endDate.getTime() - beginDate.getTime()) / (60 * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return day;
    }

    /**
     * 获得当前时间的格式化
     * 格式yyyyMMddHHmmss
     *
     * @return
     */
    public static String getDetailTime() {
        return SDF_SHORT_TIME.format(new Date());
    }

    /**
     * 获得当前时间的格式化(到毫秒)
     * 格式yyyyMMddHHmmss
     *
     * @return
     */
    public static String getLongDetailTime() {
        return SDF_LONG_TIME.format(new Date());
    }


    /**
     * 将当前时间格式化为yyyy-MM-dd HH:mm:ss格式
     *
     * @return
     */
    public static String getLongTime() {
        return DATE_TIME_ALL.format(new Date());
    }

    /**
     * 将当前时间格式化为yyyyMMddHHmmss格式
     *
     * @return
     */
    public static String getLongTimeWu() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }

    /**
     * 将当前时间格式化为yyyy-MM-dd格式
     */
    public static String getShortTime() {
        return DATE_ALL.format(new Date());
    }

    /**
     * 将当前时间格式化为yyyy-M-d格式
     */
    public static String getShortTime2() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        return df.format(new Date());
    }

    /**
     * 时间增加
     *
     * @param time 时间
     * @param type 年，月，日，周
     * @param num  要增加的数
     * @return
     * @throws ParseException
     */
    public static String increaseTheTime(String time, String type, int num) throws ParseException {
        Date date = DATE_TIME_ALL.parse(time);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        switch (type) {
            case "year":
                calendar.add(Calendar.YEAR, num);
                break;
            case "month":
                calendar.add(Calendar.MONTH, num);
                break;
            case "date":
                calendar.add(Calendar.DATE, num);
                break;
            case "week":
                calendar.add(Calendar.WEEK_OF_MONTH, num);
                break;
            default:
                break;
        }
        date = calendar.getTime();
        String dateStr = DATE_TIME_ALL.format(date);
        return dateStr;
    }

    /**
     * 判断两个日期的大小
     *
     * @param time1
     * @param time2
     * @return 时间1大于时间2 返回：1 时间1小于时间2 返回：-1
     */
    public static int comparisonTime(String time1, String time2) {
        try {
            Date dt1 = DATE_TIME_ALL.parse(time1);
            Date dt2 = DATE_TIME_ALL.parse(time2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 将指定的时间按指定的格式转换
     *
     * @param date   日期
     * @param format 格式
     * @return
     */
    public static String date2Str(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 计算与当前时间的天数时间差（已经过了：-X，还没有过：X）
     *
     * @param nowDate 当前时间 yyyy-MM-dd HH:mm:ss
     * @param time    结束时间 yyyy-MM-dd HH:mm:ss
     * @throws ParseException
     */
    public static int timeDifference(String nowDate, String time) throws ParseException {
        Date start = DATE_ALL.parse(nowDate);
        Date end = DATE_ALL.parse(time);
        //获取相减后天数
        int day = (int) ((start.getTime() - end.getTime()) / (24 * 60 * 60 * 1000));
        return day;
    }

    /**
     * 获取当月的天数
     */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据年月获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据日期 找到对应日期的 星期
     */
    public static String getDayOfWeekByDate(String date) {
        String dayOfweek = "-1";
        try {
            Date myDate = DATE_ALL.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            String str = formatter.format(myDate);
            dayOfweek = str;
        } catch (Exception e) {
            System.out.println("错误!");
        }
        return dayOfweek;
    }

    /**
     * ͨ时间相减得到天数
     *
     * @return
     * @throws ParseException
     */
    public static int differentDaysByMillisecond(String time1, String time2) throws ParseException {
        Date date1 = DATE_TIME_ALL.parse(time1);
        Date date2 = DATE_TIME_ALL.parse(time2);
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }


    /**
     * 返回当前年月日
     *
     * @return
     */
    public static String getNowDate() {
        Date date = new Date();
        String nowDate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
        return nowDate;
    }

    /**
     * 返回当前年份
     *
     * @return
     */
    public static int getYear() {
        Date date = new Date();
        String year = new SimpleDateFormat("yyyy").format(date);
        return Integer.parseInt(year);
    }

    /**
     * 返回当前月份
     *
     * @return
     */
    public static int getMonth() {
        Date date = new Date();
        String month = new SimpleDateFormat("MM").format(date);
        return Integer.parseInt(month);
    }

    //判断是不是闰年
    public static boolean isLeap(int year) {
        if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断月份有多少天
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    public static int getDays(int year, int month) {
        int days;
        int FebDay = 28;
        if (isLeap(year)) {
            FebDay = 29;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = FebDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }

    /**
     * 返回当月星期天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getSundays(int year, int month) {
        int sundays = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Calendar setDate = Calendar.getInstance();
        //从第一天开始
        int day;
        for (day = 1; day <= getDays(year, month); day++) {
            setDate.set(Calendar.DATE, day);
            String str = sdf.format(setDate.getTime());
            if (str.equals("星期日")) {
                sundays++;
            }
        }
        return sundays;
    }


    /**
     * 校验日期是否合法
     *
     * @return
     */
    public static boolean isValidDate(String s) {
        try {
            DATE_ALL.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    /**
     * 得到n天之后的日期
     *
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
        int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        String dateStr = DATE_TIME_ALL.format(date);
        return dateStr;
    }

    /**
     * 得到n天之后是周几
     *
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
        int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);

        return dateStr;
    }


    /**
     * 得到n个小时之后的时间
     *
     * @param hours
     * @return
     */
    public static String getHourSub(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
        return DATE_TIME_ALL.format(calendar.getTime());
    }

    /***
     * 得到指定时间的n个小时之后的时间
     * @param time
     * @param hours
     */
    public static String getHourSub(String time, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(fomatDate(time, "yyyy-MM-dd HH:mm:ss").getTime() + hours * 60 * 60 * 1000));
        return DATE_TIME_ALL.format(calendar.getTime());
    }

    /***
     * 得到当前时间hour小时之后的时间
     * @param hour
     */
    public static String getAfterHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        String dateTime = DATE_TIME_MINUTE.format(calendar.getTime());
        return dateTime.substring(0, dateTime.length() - 2).concat("00");
    }


    /**
     * 将Date转换成LocalDate
     *
     * @param d date
     * @return
     */
    public static LocalDate dateToLocalDate(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalDate();
    }

    /**
     * 将Date转换成LocalTime
     *
     * @param d date
     * @return
     */
    public static LocalTime dateToLocalTime(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalTime();
    }

    /**
     * 将LocalDate转换成Date
     *
     * @param localDate
     * @return date
     */
    public static Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
        return Date.from(instant);
    }


    /**
     * 将LocalDateTime转换成Date
     *
     * @param localDateTime
     * @return date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }


    /**
     * 使用Period计算两个LocalDate对象的时间差，严格按照年、月、日计算
     *
     * @param time1 起始时间
     * @param time2 结束时间
     * @return
     */
    public static Period getPeriodByLocalDate(String time1, String time2) {
        Date date1 = fomatDate(time1, "yyyy-MM-dd HH:mm:ss");
        Date date2 = fomatDate(time2, "yyyy-MM-dd HH:mm:ss");
        LocalDate day1 = dateToLocalDate(date1);
        LocalDate day2 = dateToLocalDate(date2);
        Period p = Period.between(day1, day2);
        return p;
    }

    /**
     * 字符串时间转字符串时间
     * 解决字符串转日常报 java.text.ParseException: Unparseable date 的问题
     * @param string 转换的日期
     * @return
     */
    public static String strToStr(String string) {
        String format = null;
        try {
            format = DATE_TIME_ALL.format(DEFAULT_TIME.parse(string));
        } catch (ParseException e) {
            //sdf的格式要与dateString的格式相同，否者会报错
            e.printStackTrace();
        }
        return format;
    }


}  
    

