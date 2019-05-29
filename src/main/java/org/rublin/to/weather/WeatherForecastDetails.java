package org.rublin.to.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastDetails {
    private long time;
    private String summary;
    private float temperatureHigh;
    private float temperatureLow;
    private float humidity;
    private float windSpeed;
}
