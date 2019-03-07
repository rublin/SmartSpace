package org.rublin.to.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Temperature {

    @JsonProperty("Value")
    private float value;

    @JsonProperty("Unit")
    private String unit;
}
