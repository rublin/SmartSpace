package org.rublin.to.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastResponseDto {

    private Forecast forecast;

    public List<ForecastDay> forecastDays() {
        return forecast.txtForecast.forecastday;
    }

    @Data
    public static class Forecast {
        @JsonProperty("txt_forecast")
        private TxtForecast txtForecast;
    }

    @Data
    public static class TxtForecast {
        private List<ForecastDay> forecastday;
    }

    @Data
    public static class ForecastDay {
        private String title;
        private String fcttext_metric;
        private String fcttext;
        private String icon_url;
    }
}
