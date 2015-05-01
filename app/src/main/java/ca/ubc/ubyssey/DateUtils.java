package ca.ubc.ubyssey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for managing dates
 *
 * Created by Chris Li on 3/31/2015.
 */
public class DateUtils {

    public static Date getFormattedDate(String date) {

        TimeZone timezone = TimeZone.getTimeZone("America/Los_Angeles");
        Calendar cal = Calendar.getInstance(timezone);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setCalendar(cal);
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date formattedDate = cal.getTime();

        return formattedDate;
    }

    public static String dayToString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOf = cal.get(Calendar.DAY_OF_WEEK);
        int weekOf = cal.get(Calendar.WEEK_OF_MONTH);
        int monthOf = cal.get(Calendar.MONTH);
        SimpleDateFormat time = new SimpleDateFormat("h:mm a");
        String currentTime = time.format(cal.getTime());
        Calendar curr = Calendar.getInstance();
        int currDay = curr.get(Calendar.DAY_OF_WEEK);
        int currWeek = curr.get(Calendar.WEEK_OF_MONTH);
        int currMonth = curr.get(Calendar.MONTH);
        if (weekOf == currWeek && monthOf == currMonth) {
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
            int year = cal.get(Calendar.YEAR);
            return getMonth(monthOf) + " " + dayOf + ", " + year;
        }
    }

    public static String getMonth(int month){
        String monthString = "";
        switch(month){
            case 0:
                monthString = "January";
                break;
            case 1:
                monthString = "February";
                break;
            case 2:
                monthString = "March";
                break;
            case 3:
                monthString = "April";
                break;
            case 4:
                monthString = "May";
                break;
            case 5:
                monthString = "June";
                break;
            case 6:
                monthString = "July";
                break;
            case 7:
                monthString = "August";
                break;
            case 8:
                monthString = "September";
                break;
            case 9:
                monthString = "October";
                break;
            case 10:
                monthString = "November";
                break;
            case 11:
                monthString = "December";
                break;
        }

        return monthString;
    }

    public static String getProperDateString(String date){
        return dayToString(getFormattedDate(date));
    }

}
