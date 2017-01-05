package com.example.attendancesystem.smartattend;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Rows {

    private String date;
    private String CheckOutTime;
    public String CheckInTime;
    private String mark;

    public Rows(String date, String CheckOutTime, String CheckInTime, String mark){
        this.date=date;
        this.CheckOutTime=CheckOutTime;
        this.CheckInTime=CheckInTime;
        this.mark=mark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCheckOutTime() {
        return CheckOutTime;
    }

    public String getCheckInTime() {
        return CheckInTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        CheckOutTime = checkOutTime;
    }

    public void setCheckInTime(String checkInTime) {
        CheckInTime = checkInTime;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}
