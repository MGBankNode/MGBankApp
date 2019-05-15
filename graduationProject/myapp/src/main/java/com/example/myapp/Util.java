package com.example.myapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util implements Serializable {
    public String comma(int number){return String.format("%,d", number);}
    public String dateForm(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public String dateFormToMonth(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(date);
    }
    public String dateFormToYear(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(date);
    }
}
