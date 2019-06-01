package org.rublin.to;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThingSpeakChannelSettingDto {
    private int channelId;
    private String apiKey;
    private int fieldId;
    private String fieldName;
}
