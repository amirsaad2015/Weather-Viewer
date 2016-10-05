package com.example.amir.webapi;

/**
 * Created by Amir on 9/23/2016.
 */
public class Weather {
    private String description;
    private double max;
    private double min;
    private double humidity;


    public Weather(String description, double max, double min, double humidity) {
        setDescription(description);
        setMax(max);
        setMin(min);
        setHumidity(humidity);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMax() {
        return this.max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMin() {
        return this.min;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getHumidity() {
        return humidity;
    }

    public String toString() {
        return "Weather: " + description + "\nMax: " + max + "\nMin: " + min + "\nHumidity: " + humidity + "\n\n";
    }
}
