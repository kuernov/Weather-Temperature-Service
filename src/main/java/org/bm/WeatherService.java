package org.bm;

public class WeatherService {
    public String determineCategory(double temperature){
        if (temperature < 0) return "Freezing";
        if (temperature < 10) return "Cold";
        if (temperature < 20) return "Mild";
        if (temperature < 30) return "Warm";
        return "Hot";
    } 
}
