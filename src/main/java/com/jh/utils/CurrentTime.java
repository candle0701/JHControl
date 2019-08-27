package com.jh.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentTime {
    //获取当前时间 年月日时分秒 字符串
    public static String getCurrentTimestamp(){
        // 获得当前时间
        Date getDate = Calendar.getInstance().getTime();

        String dateStr1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getDate);

        return dateStr1;
    }

    //获取当前时间 年月日 字符串
    public static String getCurrentTime(){

        Date getDate = Calendar.getInstance().getTime();

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(getDate);

        return dateStr;
    }

    public static int compare_date(String DATE1, String DATE2) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                //dt1 在dt2前
//                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //dt1在dt2后
//                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args) {
        int i= compare_date("1995-11-12 15:21", "1999-12-11 09:59");
        System.out.println("i=="+i);
    }
}
