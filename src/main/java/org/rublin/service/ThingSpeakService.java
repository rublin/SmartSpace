package org.rublin.service;

import org.rublin.to.ThingSpeakChannelSettingDto;
import org.rublin.to.ThingSpeakUploadDto;

public interface ThingSpeakService {
    void upload(ThingSpeakUploadDto uploadRequest);

    void channelSetting(ThingSpeakChannelSettingDto channelSettingRequest);
}
