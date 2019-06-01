package org.rublin.to;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThingSpeakUploadDto {
    private String apiKey;
    private int fieldId;
    private String value;
}
