package org.rublin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.rublin.service.ThingSpeakService;
import org.rublin.to.ThingSpeakChannelSettingDto;
import org.rublin.to.ThingSpeakUploadDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Slf4j
@Service
public class ThingSpeakServiceImpl implements ThingSpeakService {

    @Value("${thingspeak.url}")
    private String baseUrl;

    @Override
    public void upload(ThingSpeakUploadDto uploadRequest) {
        final String url = format("%supdate?api_key=%s&field%d=%s",
                baseUrl,
                uploadRequest.getApiKey(),
                uploadRequest.getFieldId(),
                uploadRequest.getValue());
        log.debug("Send update url: {}", url);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        log.info("Sent update for field {} with {} value. Response is {}", uploadRequest.getFieldId(), uploadRequest.getValue(), result);
    }

    @Override
    public ResponseEntity<String> channelSetting(ThingSpeakChannelSettingDto channelSettingRequest) {
        final String url = format("%schannels/%d.json",
                baseUrl,
                channelSettingRequest.getChannelId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("api_key", channelSettingRequest.getApiKey());
        map.add("field".concat(String.valueOf(channelSettingRequest.getFieldId())), channelSettingRequest.getFieldName());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
        log.info("Sent settings for filed {} and name {}; status {}", channelSettingRequest.getFieldId(), channelSettingRequest.getFieldName(),
                response.getStatusCode());
        log.debug("Response: {}", response.getBody());

        return response;
    }
}
