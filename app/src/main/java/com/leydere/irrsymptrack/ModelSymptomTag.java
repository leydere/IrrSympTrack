package com.leydere.irrsymptrack;

public class ModelSymptomTag {

    private int id;
    private String irrSymTitle;

    public ModelSymptomTag(int id, String irrSymTitle){
        this.id = id;
        this.irrSymTitle = irrSymTitle;
    }

    public ModelSymptomTag(String irrTagTitle){
        this.irrSymTitle = irrSymTitle;
    }

    //Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIrrSymTitle() {
        return irrSymTitle;
    }

    public void setIrrSymTitle(String irrSymTitle) {
        this.irrSymTitle = irrSymTitle;
    }
}
