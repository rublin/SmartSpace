package org.rublin.to.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDto {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String timezone;

    private Condition currently;
    private Forecast daily;
    private Forecast hourly;
}
