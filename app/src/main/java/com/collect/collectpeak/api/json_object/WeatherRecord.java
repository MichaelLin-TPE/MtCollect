package com.collect.collectpeak.api.json_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherRecord implements Serializable {

    @SerializedName("locations")
    private ArrayList<WeatherLocations> locations;

    public ArrayList<WeatherLocations> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<WeatherLocations> locations) {
        this.locations = locations;
    }
}
