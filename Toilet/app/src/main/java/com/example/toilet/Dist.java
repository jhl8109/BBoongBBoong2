package com.example.toilet;

public class Dist {
    private double score;
    private String address;

    public Dist(double v, String good) {
        this.score = v;
        this.address = good;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
