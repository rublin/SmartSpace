package org.rublin.service;

import org.rublin.to.ThingSpeakChannelSettingDto;
import org.rublin.to.ThingSpeakUploadDto;
import org.springframework.http.ResponseEntity;

public interface ThingSpeakService {
    void upload(ThingSpeakUploadDto uploadRequest);

    ResponseEntity<String> channelSetting(ThingSpeakChannelSettingDto channelSettingRequest);
}
