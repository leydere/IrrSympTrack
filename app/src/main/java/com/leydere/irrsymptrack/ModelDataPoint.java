package com.leydere.irrsymptrack;

public class ModelDataPoint {

    private String dpSeverity;
    private String dpDate;

    public ModelDataPoint(String dpSeverity, String dpDate) {
        this.dpSeverity = dpSeverity;
        this.dpDate = dpDate;
    }

    public String getDpSeverity() {
        return dpSeverity;
    }

    public void setDpSeverity(String dpSeverity) {
        this.dpSeverity = dpSeverity;
    }

    public String getDpDate() {
        return dpDate;
    }

    public void setDpDate(String dpDate) {
        this.dpDate = dpDate;
    }
}
