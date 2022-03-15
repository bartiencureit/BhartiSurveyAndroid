package com.encureit.samtadoot.utils;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by AbhishekR on
 */
public class CommonUtils {

    private static final String TAG = "act_CommonUtils";

    /**
     * Returns the font name.
     */

    public static String getFontName(int index) {
        String font = "fonts/Regular.ttf";
        switch (index) {
            case 2:
                font = "fonts/Bold.ttf";
                break;
        }
        return font;
    }


    // function to generate a random string of length n
    public static String getRandomAlphaNumericString() {

     return UUID.randomUUID().toString();

        // chose a Character random from this String
       /* String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }*/

      //  return sb.toString();
    }

    public static String getCurrentDate(){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            Log.e(TAG, "getCurrentDate: "+dateFormat.format(date) );
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String getCurrentDateWithDateOnly(){
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
           // Log.e(TAG, "getCurrentDate: "+dateFormat.format(date) );
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCurrentDateOnly(){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            date.getTime();
           // Log.e(TAG, "getCurrentDate: "+dateFormat.format(date) );
            return dateFormat.format(date);
    }


    public static int getDaysDifference(String startDate, String endDate ){
        String dateStart = "01/14/2012 09:29:58";
        String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(startDate);
            d2 = format.parse(endDate);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000) ;
            return (int) diffDays;
        } catch (Exception e) {
            e.printStackTrace();
            return  0;
        }
    }

    public static int dip2pix(Context context, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
                context.getResources().getDisplayMetrics());
    }

}
