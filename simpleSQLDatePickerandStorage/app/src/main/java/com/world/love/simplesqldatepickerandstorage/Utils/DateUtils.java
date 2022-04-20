package com.world.love.simplesqldatepickerandstorage.Utils;

import android.util.Log;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    final static String dateFormat = "MMM dd, yyyy" ;
    public static String getMilliSecondDate(String toParse){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(toParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return String.valueOf(millis) ;
    }

    public static String getStringDate(String milliSeconds)
    {

        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        String date =  formatter.format(calendar.getTime());
        return date ;
    }

    public static MaterialDatePicker datePicker(String title){
        // now create instance of the material date picker
        // builder make sure to add the "datePicker" which
        // is normal material date picker which is the first
        // type of the date picker in material design date
        // picker
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText(title);

        // now create the instance of the material date
        // picker
        return  materialDateBuilder.build() ;
    }

}
