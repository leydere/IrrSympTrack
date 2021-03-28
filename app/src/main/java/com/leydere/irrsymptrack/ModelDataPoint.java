package com.leydere.irrsymptrack;

public class ModelDataPoint {

    private int dpSeverity;
    private String dpDate;

    public ModelDataPoint(int dpSeverity, String dpDate) {
        this.dpSeverity = dpSeverity;
        this.dpDate = dpDate;
    }

    public int getDpSeverity() {
        return dpSeverity;
    }

    public void setDpSeverity(int dpSeverity) {
        this.dpSeverity = dpSeverity;
    }

    public String getDpDate() {
        return dpDate;
    }

    public void setDpDate(String dpDate) {
        this.dpDate = dpDate;
    }
}
