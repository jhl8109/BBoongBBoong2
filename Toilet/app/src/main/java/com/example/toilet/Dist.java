package com.example.toilet;

public class Dist implements Comparable<Dist> {
    private double score;
    private String address;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dist(String id,double v, String good) {
        this.id = id;
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

    @Override
    public int compareTo(Dist dist) {
        if (this.score > dist.score) return -1;
        else if (this.score == dist.score) return 0;
        else return 1;
    }
}
