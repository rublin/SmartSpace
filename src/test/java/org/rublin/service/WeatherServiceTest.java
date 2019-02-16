package org.rublin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {

    @Spy
    WeatherService weatherService = new WeatherService(new RestTemplate());

    @Before
    public void init() {
        ReflectionTestUtils.setField(weatherService, "token", "");
        ReflectionTestUtils.setField(weatherService, "city", "Kyiv");
        ReflectionTestUtils.setField(weatherService, "lang", "UA");
    }

    @Test
    public void getForecast() {
        String forecast = weatherService.getForecast();
        assertNotNull(forecast);
        assertTrue(forecast.length() > 40);
    }

    @Test
    public void getCondition() {
        String condition = weatherService.getCondition();
        assertNotNull(condition);
        assertTrue(condition.length() > 40);
    }
}