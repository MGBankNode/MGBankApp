package com.example.myapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public String comma(int number){return String.format("%,d", number);}
    public String dateForm(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
