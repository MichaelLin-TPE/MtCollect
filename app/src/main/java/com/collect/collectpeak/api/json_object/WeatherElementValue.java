package com.collect.collectpeak.api.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WeatherElementValue implements Serializable {
    @SerializedName("value")
    private String value;
    @SerializedName("measures")
    private String measures;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }
}
