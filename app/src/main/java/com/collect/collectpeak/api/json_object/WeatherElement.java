package com.collect.collectpeak.api.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherElement implements Serializable {
    @SerializedName("elementName")
    private String elementName;
    @SerializedName("description")
    private String description;
    @SerializedName("time")
    private ArrayList<WeatherTime> time;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<WeatherTime> getTime() {
        return time;
    }

    public void setTime(ArrayList<WeatherTime> time) {
        this.time = time;
    }
}
