package com.sun.os.bean;

public class PremiumBean {
    public String title;
    public String label;
    public int sellingPrice;
    public int originalPrice;
    public int cover;

    public PremiumBean(String title, String label, int sellingPrice, int originalPrice, int cover) {
        this.title = title;
        this.label = label;
        this.sellingPrice = sellingPrice;
        this.originalPrice = originalPrice;
        this.cover = cover;
    }
}
