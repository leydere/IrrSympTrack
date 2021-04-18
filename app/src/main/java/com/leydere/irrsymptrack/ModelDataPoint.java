package com.leydere.irrsymptrack;

/**
 * DataPoint model is used in graph creation. Each data point object is the sum of severity and the date.
 */
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
