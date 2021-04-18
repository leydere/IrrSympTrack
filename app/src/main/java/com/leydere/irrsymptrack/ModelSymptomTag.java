package com.leydere.irrsymptrack;

/**
 * Model object used in passing information back and forth between application and DB.
 */
public class ModelSymptomTag {

    private int id;
    private String symTagTitle;

    public ModelSymptomTag(int id, String symTagTitle){
        this.id = id;
        this.symTagTitle = symTagTitle;
    }

    public ModelSymptomTag(String symTagTitle){
        this.symTagTitle = symTagTitle;
    }

    //Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymTagTitle() {
        return symTagTitle;
    }

    public void setSymTagTitle(String symTagTitle) {
        this.symTagTitle = symTagTitle;
    }
}
