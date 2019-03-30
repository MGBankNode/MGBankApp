package com.example.myapp;

public class cardElement {

    private int icon;
    private String name;
    private String merit;

    public int getIcon(){return icon;}
    public String getName(){return name;}
    public String getMerit(){return merit;}


    public cardElement(int icon, String name, String content){
        this.icon=icon;
        this.name=name;
        this.merit = content;
    }
}
