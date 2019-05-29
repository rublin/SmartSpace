package org.rublin.to.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast{
    private String summary;
    private List<WeatherForecastDetails> data;
}
