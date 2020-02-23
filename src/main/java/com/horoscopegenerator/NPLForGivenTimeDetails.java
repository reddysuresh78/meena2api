package com.horoscopegenerator;

public class NPLForGivenTimeDetails {

    private String startTime;
    private String endTime;
    private Star hrm;
    private Planet hrmLord;
    private Raasi npl;

    private int hrmPada;

    private String drm;
    private Raasi hrmSign;
    private Raasi nplSign;

    private Planet hrmJeeva;
    private Planet hrmSareera;

    private Planet kalamsaLord;

    public int getHrmPada() {
        return hrmPada;
    }

    public void setHrmPada(int hrmPada) {
        this.hrmPada = hrmPada;
    }

    public Planet getKalamsaLord() {
        return kalamsaLord;
    }

    public void setKalamsaLord(Planet kalamsaLord) {
        this.kalamsaLord = kalamsaLord;
    }

    public Planet getHrmJeeva() {
        return hrmJeeva;
    }

    public void setHrmJeeva(Planet hrmJeeva) {
        this.hrmJeeva = hrmJeeva;
    }

    public Planet getHrmSareera() {
        return hrmSareera;
    }

    public void setHrmSareera(Planet hrmSareera) {
        this.hrmSareera = hrmSareera;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Star getHrm() {
        return hrm;
    }

    public void setHrm(Star hrm) {
        this.hrm = hrm;
    }

    public Planet getHrmLord() {
        return hrmLord;
    }

    public void setHrmLord(Planet hrmLord) {
        this.hrmLord = hrmLord;
    }

    public Raasi getNpl() {
        return npl;
    }

    public void setNpl(Raasi npl) {
        this.npl = npl;
    }

    public String getDrm() {
        return drm;
    }

    public void setDrm(String drm) {
        this.drm = drm;
    }

    public Raasi getHrmSign() {
        return hrmSign;
    }

    public void setHrmSign(Raasi hrmSign) {
        this.hrmSign = hrmSign;
    }

    public Raasi getNplSign() {
        return nplSign;
    }

    public void setNplSign(Raasi nplSign) {
        this.nplSign = nplSign;
    }
}
