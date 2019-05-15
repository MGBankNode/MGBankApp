package com.example.myapp;

public class menu1_rvData {
    int year, month, date;
    String hour, minute, name, card;
    int price, balance;
    String type, categori, anum;

    public menu1_rvData(int year, int month, int date, String hour, String minute, String name,
                        String card, int price, String type, String categori, int balance, String anum) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.name = name;
        this.card = card;
        this.price = price;
        this.type = type;
        this.categori = categori;
        this.balance = balance;
        this.anum = anum;
    }

    public int getDate() {
        return date;
    }

    public String getAnum() {
        return anum;
    }

    public int getMonth() {
        return month;
    }

    public int getPrice() {
        return price;
    }

    public int getYear() {
        return year;
    }

    public String getCard() {
        return card;
    }

    public String getCategori() {
        return categori;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getBalance() {
        return balance;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setCategori(String categori) {
        this.categori = categori;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}


