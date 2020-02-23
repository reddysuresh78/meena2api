package com.horoscopegenerator;

/**
 * Created by Suresh on 01-02-2018.
 */

public class NPLInfo {

    String startTime;
    String endTime;
    String star;
    String starLord;
    String raasi;

    public NPLInfo(String startTime, String endTime, String star, String starLord, String raasi) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.star = star;
        this.starLord = starLord;
        this.raasi = raasi;
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

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getStarLord() {
        return starLord;
    }

    public void setStarLord(String starLord) {
        this.starLord = starLord;
    }

    public String getRaasi() {
        return raasi;
    }

    public void setRaasi(String raasi) {
        this.raasi = raasi;
    }


}
