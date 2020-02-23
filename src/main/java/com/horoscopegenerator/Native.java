package com.horoscopegenerator;

/**
 * Created by Suresh on 11-02-2017.
 */

public class Native {

    private String refNo;
    private String name;
    private String nameInChart;
    private String date;
    private String time;
    private String place;
    private double latitude;
    private double longitude;
    private boolean generateChart;
    private String timeZone;
    private int offset;

    public boolean isGenerateChart() {
        return generateChart;
    }

    public void setGenerateChart(boolean generateChart) {
        this.generateChart = generateChart;
    }


    public Native(String csvLine){

        String[] values = csvLine.split(",");

        this.refNo = values[0];
        this.name = values[1];
        this.nameInChart = values[2];
        this.date = values[3] ;
        this.time = values[4];
        this.place = values[5];
        this.latitude = getDoubleValue(values[6]);
        this.longitude = getDoubleValue(values[7]);
        this.timeZone = values[8];
        this.offset = Integer.parseInt(values[9]);
        this.generateChart = "Y".equalsIgnoreCase(values[10])? true:false;

    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getNameInChart() {
        return nameInChart;
    }

    public String getPlace() {
        return place;
    }

    public String getRefNo() {
        return refNo;
    }

    private static double getDoubleValue(String value) {
        //Example 24#19
        boolean isNegative = false;
        if(value.startsWith("-")){
            isNegative = true;
            value = value.substring(1);
        }
        String[] values = value.split("#");
        double val =  Integer.parseInt(values[0]) + Integer.parseInt(values[1]) / 60.0D + 0 / 3600.0D;
        if(isNegative){
            return -val;
        }else{
            return val;
        }
    }


}
