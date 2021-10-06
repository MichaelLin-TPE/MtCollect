package com.collect.collectpeak.api.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherLocation implements Serializable {

    @SerializedName("locationName")
    private String locationName;
    @SerializedName("lat")
    private String lat;
    @SerializedName("lon")
    private String lon;
    @SerializedName("weatherElement")
    private ArrayList<WeatherElement> weatherElement;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public ArrayList<WeatherElement> getWeatherElement() {
        return weatherElement;
    }

    public void setWeatherElement(ArrayList<WeatherElement> weatherElement) {
        this.weatherElement = weatherElement;
    }
}
