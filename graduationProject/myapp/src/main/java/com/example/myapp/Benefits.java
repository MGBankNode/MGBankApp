package com.example.myapp;

import java.util.ArrayList;
import java.util.Arrays;

public class Benefits implements CardDiscount{
    private String b_name;
    private ArrayList<String> benefitsNames;
    private int benefitValue;
    public Benefits(String b_name, String[] list, int value){
        this.b_name = b_name;
        this.benefitsNames = new ArrayList<String>(Arrays.asList(list));
        this.benefitValue = value;
    }
    public String getB_name(){return b_name;}
    public ArrayList<String> getBenefitsNames(){return benefitsNames;}
    public int getBenefitValue(){return benefitValue;}
    @Override
    public boolean discountCondition() {
        return true;
    }
}
