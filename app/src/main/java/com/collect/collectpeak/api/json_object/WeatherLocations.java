package com.collect.collectpeak.api.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherLocations implements Serializable {
    @SerializedName("location")
    private ArrayList<WeatherLocation> location;

    public ArrayList<WeatherLocation> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<WeatherLocation> location) {
        this.location = location;
    }
}
