package org.rublin.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.rublin.service.ThingSpeakService;
import org.rublin.to.ThingSpeakChannelSettingDto;
import org.rublin.to.ThingSpeakUploadDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ThingSpeakServiceImplTest {
    private static final String API_KEY = "BQ1GR...";
    private static final String WRITE_API_KEY = "LTM0...";

    ThingSpeakService thingSpeakService = new ThingSpeakServiceImpl();

    @Before
    public void init() {
        System.setProperty("log4j.logger.httpclient.wire", "DEBUG");
        ReflectionTestUtils.setField(thingSpeakService, "baseUrl", "https://api.thingspeak.com/");
    }

    @Test
    public void putTest() {
        final String url = "https://api.thingspeak.com/channels/613710.json";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("api_key", API_KEY);
        param.add("field1", "test811");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);

        assertEquals(200, response.getStatusCodeValue());
        System.out.println(response.getBody());
    }

    @Test
    public void upload() {
        thingSpeakService.upload(ThingSpeakUploadDto.builder()
                .fieldId(1)
                .value("20.1")
                .apiKey(WRITE_API_KEY)
                .build());
    }

    @Test
    public void channelSetting() {
        ResponseEntity<String> response = thingSpeakService.channelSetting(ThingSpeakChannelSettingDto.builder()
                .channelId(613710)
                .apiKey(API_KEY)
                .fieldId(1)
                .fieldName("test")
                .build());
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        System.out.println(response.getBody());
    }
}