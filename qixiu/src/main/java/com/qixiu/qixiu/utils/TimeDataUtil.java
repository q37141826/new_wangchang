package com.qixiu.qixiu.utils;

import android.app.Activity;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;

import com.qixiu.wigit.picker.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HuiHeZe on 2017/8/25.
 */

public class TimeDataUtil {
    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年
    private final static String DEFULT_TIME_FORMAT = "yyyy-MM-dd hh:mm";
    private final static String DEFULT_DATE_FORMAT = "yyyy-MM-dd";

    public static String getDate(long addTime) {
        String time = "";
        Time t = new Time();
        t.set(System.currentTimeMillis() + addTime);
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int minute = t.minute;
        int hour = t.hour;
        String months = month + "";
        String days = day + "";
        if (month < 10) {
            months = 0 + "" + months;
        }
        if (day < 10) {
            days = 0 + "" + day;
        }
        time = "" + year + "/" + months + "/" + days;
        Log.e("time", System.currentTimeMillis() + "添加的时间" + addTime + "最后解析为" + time);
        return time;
    }


    public static String getTime(long addTime) {
        String time = "";
        Time t = new Time();
        t.set(System.currentTimeMillis() + addTime);
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int minute = t.minute;
        int hour = t.hour;
        String months = month + "";
        String days = day + "";
        if (month < 10) {
            months = 0 + "" + months;
        }
        if (day < 10) {
            days = 0 + "" + day;
        }
        if (hour < 10) {
            time = "" + "0" + hour + ":00";
        } else {
            time = "" + hour + ":00";
        }
        return time + "";
    }

    public static String getTimeToDay() {
        String mYear;
        String mMonth;
        String mDay;
        Calendar ca = Calendar.getInstance();
       mYear = ca.get(Calendar.YEAR) + "";

        if (ca.get(Calendar.MONTH) < 10) {
            mMonth = "0" + (ca.get(Calendar.MONTH) + 1);
        } else {
            mMonth = "" + (ca.get(Calendar.MONTH) + 1);
        }

        if (ca.get(Calendar.DAY_OF_MONTH) < 10) {
            mDay = "0" + ca.get(Calendar.DAY_OF_MONTH);
        } else {
            mDay = "" + ca.get(Calendar.DAY_OF_MONTH);
        }
        return mYear+"-"+mMonth+"-"+mDay;
    }

//    public static String getTimeStamp(long addTime) {
//        String time = "";
//        Time t = new Time();
//        t.set(System.currentTimeMillis() + addTime);
//        int year = t.year;
//        int month = t.month + 1;
//        int day = t.monthDay;
//        int minute = t.minute;
//        int hour = t.hour;
//        String months = month + "";
//        String days = day + "";
//        if (month < 10) {
//            months = 0 + "" + months;
//        }
//        if (day < 10) {
//            days = 0 + "" + day;
//        }
//        if (hour < 10) {
//            time = "" + "0" + hour + ":00";
//        } else {
//            time = "" + hour + ":00";
//        }
//        return year + "" + months + days + time + "";
//    }

    public static String getTimeStamp(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat formate = new SimpleDateFormat(format);
        return formate.format(date);
    }

    public static String getTimeStamp(long time) {
        return getTimeStamp(time,DEFULT_DATE_FORMAT);
    }

    public static MyDateBean parseLong(long addTime) {
        Time t = new Time();
        t.set(addTime);
        MyDateBean myDateBean = new MyDateBean();
        myDateBean.setYear(t.year);
        myDateBean.setMonth(t.month + 1);
        myDateBean.setData(t.monthDay);
        myDateBean.setHour(t.hour);
        myDateBean.setMinute(t.minute);
        myDateBean.setSeconds(t.second);
        return myDateBean;
    }

    public static boolean isSameDay(long time01, long time02) {
        MyDateBean myDateBean01 = parseLong(time01);
        MyDateBean myDateBean02 = parseLong(time02);
        if (myDateBean01.getYear() == myDateBean02.getYear() && myDateBean01.getMonth() == myDateBean02.getMonth() && myDateBean01.getData() == myDateBean02.getData()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isToday(long time) {
        return isSameDay(time, System.currentTimeMillis());
    }


    public static List<String> getDataList(int days) {
        List<String> list = new ArrayList<>();
        for (long i = 0; i < days; i++) {
            String date = getDate(i * 24 * 3600 * 1000);
            list.add(date);
        }
        return list;
    }

    public static List<String> getTimeList(int times) {
        List<String> list = new ArrayList<>();
        for (long i = 0; i < times; i++) {
            String date = getTime(i * 3600 * 1000);
            list.add(date);
        }
        return list;
    }

    public static List<String> getTimtArea(int now) {
        if (now > 11) {
            return null;
        }
        List<String> list = new ArrayList<>();
        String time[] = {"9:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00", "19:00-20:00"};
        for (int i = now; i < time.length; i++) {
            list.add(time[i]);
        }
        return list;
    }

    public static int getTime() {
        int timeAreas = 0;
        Time time = new Time();
        time.setToNow();
        if (time.hour > 9 && time.hour < 20) {
            timeAreas = time.hour - 9;
        }
        return timeAreas;
    }

    public static boolean isToday(String time) {
        String date = getDate(0);
        if ((date + "").equals(time)) {
            return true;
        }
        return false;
    }

    public static final int BEFORE_SECTION = 001, AFTER_SECTION = 002, DURING_SECTION = 003;

    public static int getTimeSection(int before, int after) {
        int timeAreas = 0;
        Time time = new Time();
        time.setToNow();
        if (time.hour < before) {
            return BEFORE_SECTION;
        } else if (time.hour > after) {
            return AFTER_SECTION;
        } else {
            return DURING_SECTION;
        }
    }

    public int getYear() {
        String time = "";
        Time t = new Time();
        t.set(System.currentTimeMillis());
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int minute = t.minute;
        int hour = t.hour;
        return year;
    }

    public int getMonth() {
        String time = "";
        Time t = new Time();
        t.set(System.currentTimeMillis());
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int minute = t.minute;
        int hour = t.hour;
        return month;
    }

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    public static String dateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String str = format.format(date);
        return str;
    }

    public static String dateToStr(Date date, String format) {
        SimpleDateFormat formats = new SimpleDateFormat(format);
        String str = formats.format(date);
        return str;
    }

    public static Date strToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date strToDate(String str, String formats) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formats);
        Date date = null;
        try {
            date = dateformat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getBefore(long seconds) {
        Date date = new Date();
        long countdown = date.getTime() - seconds;
        String str = "";
        if (countdown / 1000 < 60) {
            str = "刚刚";
        } else if ((countdown / 1000) >= 60 && (countdown / 1000) < 3600) {
            str = countdown / 1000 / 60 + "分钟前";
        } else if ((countdown / 1000) >= 3600 && (countdown / 1000) < (3600 * 24)) {
            str = countdown / 1000 / 60 / 60 + "小时前";
        } else if ((countdown / 1000) >= (3600 * 24 * 20)) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(seconds);
            str = formatter.format(calendar.getTime());
        } else {
            str = countdown / 1000 / 60 / 60 / 24 + "天前";
        }
        return str;
    }

    public static String getBefore(String str) {
        try {
            return getBefore(strToDate(str).getTime());
        } catch (Exception e) {
            return str;
        }
    }

    public static boolean is_early(long before, long after) {
        return after - before >= 0;
    }

    public static boolean is_early(String before, String after, String format) {
        Date date_before = strToDate(before, format);
        Date date_after = strToDate(after, format);
        long bef = date_before.getTime();
        long aft = date_after.getTime();
        return is_early(bef, aft);
    }


    public static class MyDateBean {
        private int year;
        private int month;
        private int data;
        private int hour;
        private int minute;
        private int seconds;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSeconds() {
            return seconds;
        }

        public void setSeconds(int seconds) {
            this.seconds = seconds;
        }
    }


    public static void showDatePickers(final TextView text, Activity activity) {
        DatePicker mDatePicker = new DatePicker(activity);
        mDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                text.setText(year + "-" + month + "-" + day);
            }
        });
        mDatePicker.show();
    }
    public static void showYearMothPickers(final TextView text, Activity activity) {
        DatePicker mDatePicker = new DatePicker(activity,1);
        mDatePicker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                text.setText(year + "-" + month);
            }

        });
        mDatePicker.show();
    }

}
