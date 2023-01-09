package com.pab.mooneyq.utillities;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtil {

    public static String number(Integer number) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    public static String date(String string){
        try {
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date parser = date.parse( string );
            SimpleDateFormat formatter = new SimpleDateFormat("MMM, dd yyyy", Locale.getDefault());
            return formatter.format( parser );
        } catch (ParseException exception) {
            return "";
        }
    }

}
