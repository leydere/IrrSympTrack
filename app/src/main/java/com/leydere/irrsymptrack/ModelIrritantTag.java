package com.leydere.irrsymptrack;

public class ModelIrritantTag {

    private int id;
    private String irrTagTitle;

    public ModelIrritantTag(int id, String irrTagTitle){
        this.id = id;
        this.irrTagTitle = irrTagTitle;
    }

    public ModelIrritantTag(String irrTagTitle){
        this.irrTagTitle = irrTagTitle;
    }

    //Getters & Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIrrTagTitle() {
        return irrTagTitle;
    }

    public void setIrrTagTitle(String irrTagTitle) {
        this.irrTagTitle = irrTagTitle;
    }
}
