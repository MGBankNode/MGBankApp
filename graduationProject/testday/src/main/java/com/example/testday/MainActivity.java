package com.example.testday;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<Date> DayList = new ArrayList<>();

    int pYear;
    int pMonth;
    int pDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String inputDate = "2019-05-12";
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
//        Date nDate = null;
//        try {
//            nDate = dateFormat.parse(inputDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Calendar cal = Calendar.getInstance() ;
//        cal.setTime(nDate);
//
//        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
//
//        Log.d(">>>", String.valueOf(dayNum));
//
        Date nDate = null;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        pYear = cal.get ( cal.YEAR );
        pMonth = cal.get ( cal.MONTH ) + 1 ;
        pDate = cal.get ( cal.DATE ) ;








    }

    public static String getSunday(String yyyy,String mm, String wk){

        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy.MM.dd");

        Calendar c = Calendar.getInstance();

        int y=Integer.parseInt(yyyy);
        int m=Integer.parseInt(mm)-1;
        int w=Integer.parseInt(wk);

        c.set(Calendar.YEAR,y);
        c.set(Calendar.MONTH,m);
        c.set(Calendar.WEEK_OF_MONTH,w);
        c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        c.add(c.DATE,7);

        return formatter.format(c.getTime());

    }




}
