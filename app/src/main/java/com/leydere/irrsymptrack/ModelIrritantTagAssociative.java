package com.leydere.irrsymptrack;

public class ModelIrritantTagAssociative {

    private int irrId;
    private int tagId;

    public ModelIrritantTagAssociative(int irrId, int tagId){
        this.irrId = irrId;
        this.tagId = tagId;
    }

    //Getters & Setters

    public int getIrrId() {
        return irrId;
    }

    public void setIrrId(int irrId) {
        this.irrId = irrId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
