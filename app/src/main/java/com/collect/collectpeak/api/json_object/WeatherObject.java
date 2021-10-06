package com.collect.collectpeak.api.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherObject implements Serializable {

    @SerializedName("records")
    private WeatherRecord records;

    private String targetLocation;

    public String getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }

    public WeatherRecord getRecords() {
        return records;
    }

    public void setRecords(WeatherRecord records) {
        this.records = records;
    }
}
