package com.findwise.model;

public class IndexEntryImpl implements IndexEntry {

    private String id;

    private double score;

    public IndexEntryImpl(String id, double score) {
        this.id = id;
        this.score = score;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "[" + id + ", TF-IDF: " + score + "]";
    }

}
