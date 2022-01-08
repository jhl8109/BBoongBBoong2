package com.example.toilet;

public class Review {
    private double score;
    private String comment;

    public Review(double v, String good) {
        this.score = v;
        this.comment = good;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
