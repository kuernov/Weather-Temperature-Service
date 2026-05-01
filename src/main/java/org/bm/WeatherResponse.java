package org.bm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeatherResponse {
    private String city;
    private double temperature;
    private String category;
}