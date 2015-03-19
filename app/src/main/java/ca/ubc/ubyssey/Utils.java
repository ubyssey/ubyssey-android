package ca.ubc.ubyssey;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for various functions used throughout the app
 * <p/>
 * Created by Chris Li on 3/17/2015.
 */
public class Utils {


    public static Date getFormattedDate(String date) {
        Date formattedDate;
        DateFormat format = new SimpleDateFormat("MMM d, yyyy hh:mm a", Locale.ENGLISH);
        try {
            formattedDate = format.parse(date);
        } catch (ParseException e) {
            formattedDate = null;
            e.printStackTrace();
        }

        return formattedDate;
    }

    public static String dayToString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOf = cal.get(Calendar.DAY_OF_WEEK);
        int weekOf = cal.get(Calendar.WEEK_OF_MONTH);
        SimpleDateFormat time = new SimpleDateFormat("h:mm a");
        String currentTime = time.format(cal.getTime());
        Calendar curr = Calendar.getInstance();
        int currDay = curr.get(Calendar.DAY_OF_WEEK);
        int currWeek = curr.get(Calendar.WEEK_OF_MONTH);
        if (weekOf == currWeek) {
            if (dayOf == currDay) {
                return "Today" + " " + currentTime;
            } else if (dayOf == Calendar.SUNDAY) {
                return "Sunday" + " " + currentTime;
            } else if (dayOf == Calendar.MONDAY) {
                return "Monday" + " " + currentTime;
            } else if (dayOf == Calendar.TUESDAY) {
                return "Tuesday" + " " + currentTime;
            } else if (dayOf == Calendar.WEDNESDAY) {
                return "Wednesday" + " " + currentTime;
            } else if (dayOf == Calendar.THURSDAY) {
                return "Thursday" + " " + currentTime;
            } else if (dayOf == Calendar.FRIDAY) {
                return "Friday" + " " + currentTime;
            } else {
                return "Saturday" + " " + currentTime;
            }
        } else {
            return date.toString();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
