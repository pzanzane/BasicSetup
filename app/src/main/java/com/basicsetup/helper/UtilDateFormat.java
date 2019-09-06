package com.basicsetup.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pankaj on 11/8/15.
 */
public class UtilDateFormat {

    public static final String DD_MM_YY="dd-MM-yy";
    public static final String yyyy_MM_dd_T_HH_mm_ss="yyyy-MM-dd'T'HH:mm:ss";


    public static String format(String fromPattern,String toPattern,String date) throws ParseException {

        SimpleDateFormat formater = new SimpleDateFormat(fromPattern);
        Date d = formater.parse(date);

        SimpleDateFormat formatTo = new SimpleDateFormat(toPattern);

        return formatTo.format(d);

    }

    public static String format(String pattern,Date date){

        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        return formater.format(date);

    }

    public static String add(String dateString,int field,int value) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YY);

        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(dateString));
        c.add(field,value);

        return sdf.format(c.getTime());

    }
}
