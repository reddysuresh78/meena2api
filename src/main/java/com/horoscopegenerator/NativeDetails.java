package com.horoscopegenerator;

import java.util.Map;

public class NativeDetails {

	public String nativeName;
    public String dateOfBirth;
    public String timeOfBirth;
    public String placeOfBirth;

    public double longitude;
    public double latitude;

    public int offset;

    public String timeZoneId;

    public String filePath;

    public Map<String, byte[]> imageData;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, byte[]> getImageData() {
        return imageData;
    }

    public void setImageData(Map<String, byte[]> imageData) {
        this.imageData = imageData;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getTimeOfBirth() {
        return timeOfBirth;
    }

    public void setTimeOfBirth(String timeOfBirth) {
        this.timeOfBirth = timeOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    @Override
    public String toString() {
        return "NativeDetails{" +
                "nativeName='" + nativeName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", timeOfBirth='" + timeOfBirth + '\'' +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", offset=" + offset +
                ", timeZoneId='" + timeZoneId + '\'' +
                '}';
    }
}
