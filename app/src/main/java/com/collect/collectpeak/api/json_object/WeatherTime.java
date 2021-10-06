package com.collect.collectpeak.api.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherTime implements Serializable {
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("endTime")
    private String endTime;
    @SerializedName("elementValue")
    private ArrayList<WeatherElementValue> elementValue;

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

    public ArrayList<WeatherElementValue> getElementValue() {
        return elementValue;
    }

    public void setElementValue(ArrayList<WeatherElementValue> elementValue) {
        this.elementValue = elementValue;
    }
}
