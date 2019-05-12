package com.example.myapp;

public class DateActivity {

    private int year;
    private int month;
    private int day;
    private int dayOfWeek;

    final int February = 28;
    final int OddMonth = 31;
    final int EvenMonth = 30;

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public DateActivity(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateActivity(int year, int month, int day, int dayOfWeek) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayOfWeek = dayOfWeek;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public DateActivity getLastSunday() {

        int mYear = this.year;
        int mMonth = this.month;
        int mDay = this.day;

        mDay -= 7;

        if(mDay <= 0) {
            mMonth--;

            if(mMonth < 1) {
                mMonth = 12;
                mYear--;
            }

            if(mMonth == 2) { // 2월
                mDay += February;
            }
            else if(mMonth % 2 == 1 || mMonth == 8) { // 홀수 달
                mDay += OddMonth;
            }
            else if(mMonth % 2 == 0 ) { // 짝수 달
                mDay += EvenMonth;
            }
        }

        return new DateActivity(mYear, mMonth, mDay);
    }
    public DateActivity recoverDate() {
        int mYear = this.year;
        int mMonth = this.month;
        int mDay = this.day;

        mDay += 7;
        // 4월 30일 29일에서 36일 6일이됨
        if(mMonth == 2) {
            if(mDay > February) {
                mDay -= February;
                mMonth++;
            }
        }
        else if(mMonth % 2 == 1 || mMonth == 8) {
            if(mDay > OddMonth) {
                mDay -= OddMonth;
                mMonth++;
            }
        }
        else if(mMonth % 2 == 0) {
            if(mDay > EvenMonth) {
                mDay -= EvenMonth;
                mMonth++;
            }
        }
        if(mMonth > 12)
            mMonth = 1;

        return new DateActivity(mYear, mMonth, mDay);
    }
}
