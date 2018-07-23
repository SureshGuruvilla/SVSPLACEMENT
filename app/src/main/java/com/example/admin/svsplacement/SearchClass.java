package com.example.admin.svsplacement;

/**
 * Created by sabari on 4/13/2018.
 */

public class SearchClass {
    private String NAME,TM,TWM,CGPA,SNO,REGNO,DEPT;

    public SearchClass(String NAME, String TM, String TWM, String CGPA, String SNO, String REGNO, String DEPT) {
        this.NAME = NAME;
        this.TM = TM;
        this.TWM = TWM;
        this.CGPA = CGPA;
        this.SNO = SNO;
        this.REGNO = REGNO;
        this.DEPT = DEPT;
    }

    public SearchClass() {
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTM() {
        return TM;
    }

    public void setTM(String TM) {
        this.TM = TM;
    }

    public String getTWM() {
        return TWM;
    }

    public void setTWM(String TWM) {
        this.TWM = TWM;
    }

    public String getCGPA() {
        return CGPA;
    }

    public void setCGPA(String CGPA) {
        this.CGPA = CGPA;
    }

    public String getSNO() {
        return SNO;
    }

    public void setSNO(String SNO) {
        this.SNO = SNO;
    }

    public String getREGNO() {
        return REGNO;
    }

    public void setREGNO(String REGNO) {
        this.REGNO = REGNO;
    }

    public String getDEPT() {
        return DEPT;
    }

    public void setDEPT(String DEPT) {
        this.DEPT = DEPT;
    }
}
