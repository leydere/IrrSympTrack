package com.leydere.irrsymptrack;

//TODO: Check to see where I had intended to use this model object.  Remove if unnecessary.
/**
 * Model object used in passing information back and forth between application and DB.
 */
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
