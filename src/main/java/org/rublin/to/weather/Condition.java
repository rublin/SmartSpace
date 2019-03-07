package org.rublin.to.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Condition {
    private long time;
    private String summary;
    private float temperature;
    private float humidity;
    private float pressure;
    private float windSpeed;
}
