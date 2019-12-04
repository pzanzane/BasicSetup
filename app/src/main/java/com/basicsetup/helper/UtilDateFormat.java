package com.meditation.live.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilDateFormat {

    public static final String DD_MM_YY="dd/MM/yy";
    public static final String DD_MMM_YY="dd MMM, yy";
    public static final String EEE_MMM_d="EEE, MMM d";
    public static final String yyyy_MM_dd_T_HH_mm_ss="yyyy-MM-dd'T'HH:mm:ss";
    public static final String yyyy_MM_dd="yyyy-MM-dd";


    public static String appendOrdinalSuffixToDate(String strDate, String dateFormat) throws ParseException {
        int indexOfDD = dateFormat.lastIndexOf("d") +1;
        Date date = toDate(dateFormat, strDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String leftString = strDate.substring(0, indexOfDD) + getOrdinalNumberSuffix(day);
        String rightString = strDate.substring(indexOfDD);
        return leftString+" "+rightString;
    }

    public static String format(String fromPattern,String toPattern,String date) throws ParseException {

        SimpleDateFormat formater = new SimpleDateFormat(fromPattern);
        Date d = formater.parse(date);

        SimpleDateFormat formatTo = new SimpleDateFormat(toPattern);
        String formatedDate = formatTo.format(d);

        return formatedDate;

    }
    public static String formatWithOrdinalSuffix(String fromPattern,String toPattern,String date) throws ParseException {


        String formatedDate = format(fromPattern, toPattern, date);

        //Append Ordinal Suffix
        formatedDate = appendOrdinalSuffixToDate(formatedDate, toPattern);
        return formatedDate;

    }

    public static String format(String pattern,Date date){

        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        return formater.format(date);

    }

    public static Date toDate(String pattern, String date) throws ParseException {

        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        Date d = formater.parse(date);
        return d;
    }
    public static String add(String dateString,int field,int value) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YY);

        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dateString));
        c.add(field,value);

        return sdf.format(c.getTime());

    }

    public static String getOrdinalNumberSuffix(int number){

        if (number >= 11 && number <= 13) {
            return "th";
        }
        switch (number % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }

    }
}
