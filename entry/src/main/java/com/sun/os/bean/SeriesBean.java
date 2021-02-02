package com.sun.os.bean;

public class SeriesBean {
    public String title;
    public String[] labels;
    public int cover;

    public SeriesBean(String title, String[] labels, int cover) {
        this.title = title;
        this.labels = labels;
        this.cover = cover;
    }
}
