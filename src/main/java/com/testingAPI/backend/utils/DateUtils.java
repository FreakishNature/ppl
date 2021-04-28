package com.testingAPI.backend.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    TimeZone gmt = TimeZone.getTimeZone("GMT");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public DateUtils(TimeZone gmt, String dateFormat){
        this.gmt = gmt;
        sdf = new SimpleDateFormat(dateFormat);
    }
    public DateUtils(){}

    public Date getPastDate(){ return getPastDate(1);};
    public Date getFutureDate(){ return getFutureDate(1);};

    public Date getPastDate(int monthsInPast){
        return getFutureDate(-monthsInPast);
    }
    public Date getFutureDate(int monthsInFuture){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH,monthsInFuture);
        return c.getTime();
    }

    public Date getNextDay(int daysAfter){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK,daysAfter);
        return c.getTime();
    }
    public Date getPreviousDay(int daysBefore){
        return getFutureDate(-daysBefore);
    }
    public Date getNextDay(){
        return getFutureDate(1);
    }
    public Date getPreviousDay(){
        return getPreviousDay(1);
    }

    public Date getCurrentDate() { return new Date(); }


}
