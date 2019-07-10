package com.example.helloworld.recycler_view;

public class Data {

    private String info;
    private String title;
    private int hotValue;

    //public static int isTopThree = 1;

    public Data(String info) { this.info = info; }
    public Data(String info, String title, int hotValue) {
        this.info = info;
        this.title = title;
        this.hotValue = hotValue;
    }

    public String getInfo() { return info; }
    public String getTitle() { return title; }
    public int getHotValue() { return hotValue; }

    public void setIndex(String info) { this.info = info; }
}
