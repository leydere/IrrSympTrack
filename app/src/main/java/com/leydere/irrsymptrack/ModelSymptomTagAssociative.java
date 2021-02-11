package com.leydere.irrsymptrack;

public class ModelSymptomTagAssociative {

    private int symId;
    private int tagId;

    public ModelSymptomTagAssociative(int symId, int tagId){
        this.symId = symId;
        this.tagId = tagId;
    }

    //Getters & Setters

    public int getSymId() {
        return symId;
    }

    public void setSymId(int symId) {
        this.symId = symId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
