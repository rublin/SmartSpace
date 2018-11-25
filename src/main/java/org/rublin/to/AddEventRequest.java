package org.rublin.to;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddEventRequest {
    private Integer sensorId;
    private String value;
}
