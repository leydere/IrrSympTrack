package com.leydere.irrsymptrack;

public class ModelSymptom {

    private int id;
    private String symTitle;
    private String symTimeDate;
    private String symSeverity;
    private String symImagePath;

    public ModelSymptom(int id, String symTitle, String symTimeDate, String symSeverity, String symImagePath){
        this.id = id;
        this.symTitle = symTitle;
        this.symTimeDate = symTimeDate;
        this.symSeverity = symSeverity;
        this.symImagePath = symImagePath;
    }

    public ModelSymptom(String symTitle, String symTimeDate, String symSeverity, String symImagePath){
        this.symTitle = symTitle;
        this.symTimeDate = symTimeDate;
        this.symSeverity = symSeverity;
        this.symImagePath = symImagePath;
    }

    //skipped "toString" method included in old work

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymTitle() {
        return symTitle;
    }

    public void setSymTitle(String symTitle) {
        this.symTitle = symTitle;
    }

    public String getSymTimeDate() {
        return symTimeDate;
    }

    public void setSymTimeDate(String symTimeDate) {
        this.symTimeDate = symTimeDate;
    }

    public String getSymSeverity() {
        return symSeverity;
    }

    public void setSymSeverity(String symSeverity) {
        this.symSeverity = symSeverity;
    }

    public String getSymImagePath() {
        return symImagePath;
    }

    public void setSymImagePath(String symImagePath) {
        this.symImagePath = symImagePath;
    }
}
