package com.leydere.irrsymptrack;

/**
 * Model object used in passing information back and forth between application and DB.
 */
public class ModelIrritant {

    private int id;
    private String irrTitle;
    private String irrTimeDate;
    private int irrSeverity;

    public ModelIrritant(int id, String irrTitle, String irrTimeDate, int irrSeverity){
        this.id = id;
        this.irrTitle = irrTitle;
        this.irrTimeDate = irrTimeDate;
        this.irrSeverity = irrSeverity;
    }

    public ModelIrritant(String irrTitle, String irrTimeDate, int irrSeverity){
        this.irrTitle = irrTitle;
        this.irrTimeDate = irrTimeDate;
        this.irrSeverity = irrSeverity;
    }

    //skipped "toString" method included in old work

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIrrTitle() {
        return irrTitle;
    }

    public void setIrrTitle(String irrTitle) {
        this.irrTitle = irrTitle;
    }

    public String getIrrTimeDate() {
        return irrTimeDate;
    }

    public void setIrrTimeDate(String irrTimeDate) {
        this.irrTimeDate = irrTimeDate;
    }

    public int getIrrSeverity() {
        return irrSeverity;
    }

    public void setIrrSeverity(int irrSeverity) {
        this.irrSeverity = irrSeverity;
    }
}
