package com.horoscopegenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {


    public static String toHMS(double d) {
        d += 0.5/3600.;	// round to one second
        int h = (int) d;
        d = (d - h) * 60;
        int min = (int) d;
        int sec = (int)((d - min) * 60);

        return String.format("%2d:%02d:%02d", h, min, sec);
    }

    public static String toDMS(double d) {
        d += 0.5/3600./10000.;	// round to 1/1000 of a second
        int deg = (int) d;
        d = (d - deg) * 60;
        int min = (int) d;
        d = (d - min) * 60;
        double sec = d;

        return String.format("%3d°%02d'", deg, min, sec);
    }

    public static String getDDMMYY(Calendar calendar) {
        String retVal = "";

        String day = Integer.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).toString();
        String month = Integer.valueOf(calendar.get(Calendar.MONTH) + 1).toString();
        String year = Integer.valueOf(calendar.get(Calendar.YEAR)).toString();

        retVal = day.length() < 2 ? "0" + day : day;
        retVal = retVal + "-" + (month.length() < 2 ? "0" + month : month);
        retVal = retVal + "-" + (year.length() < 2 ? "0" + year : year.substring(year.length() - 2, year.length()));

        return retVal;
    }

    public static SimpleDateFormat HH_MM_FORMAT = new SimpleDateFormat("hh:mm a");
    public static String getDisplayDateHHMM(Calendar calendar){

        return HH_MM_FORMAT.format(calendar.getTime());
    }

    public static SimpleDateFormat HH_MM_SS_FORMAT = new SimpleDateFormat("hh:mm:ss a");
    public static String getDisplayDateHHMMSS(Calendar calendar){

        return HH_MM_SS_FORMAT.format(calendar.getTime());
    }


    public static SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("hh:mm:ss");
    public static String getDisplayDate(Calendar calendar){

        return HOUR_FORMAT.format(calendar.getTime());
    }

    public static Calendar getTimeFromString(String time){

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(HH_MM_FORMAT.parse(time));// all done
        }
        catch(Exception e){

        }
        return cal;
    }
    public static SimpleDateFormat HOUR24_FORMAT = new SimpleDateFormat("HH:mm");
    public static Calendar getTimeFromString24(String time){

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(HOUR24_FORMAT.parse(time));// all done
        }
        catch(Exception e){

        }
        return cal;
    }

    public static String getInitCap(String input){
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase() ;
    }

}
