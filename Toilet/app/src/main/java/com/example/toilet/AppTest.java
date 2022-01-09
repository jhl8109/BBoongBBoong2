package com.example.toilet;

import android.app.Application;

import java.util.ArrayList;

public class AppTest extends Application {
    private ArrayList<Result> toiletList;
    private ArrayList<Result> trashList;

    public ArrayList<Result> getToiletList() {
        return toiletList;
    }

    public void setToiletList(ArrayList<Result> toiletList) {
        this.toiletList = toiletList;
    }

    public ArrayList<Result> getTrashList() {
        return trashList;
    }

    public void setTrashList(ArrayList<Result> trashList) {
        this.trashList = trashList;
    }
}