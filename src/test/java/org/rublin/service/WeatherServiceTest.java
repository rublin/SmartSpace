package org.rublin.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {

    @Spy
    WeatherService weatherService = new WeatherService(new RestTemplate());

    @Before
    public void init() {
        ReflectionTestUtils.setField(weatherService, "token", "c3bac5d504802...");
        ReflectionTestUtils.setField(weatherService, "coordinates", "50.378763,31.322262");
        ReflectionTestUtils.setField(weatherService, "url", "https://api.darksky.net/forecast/%s/%s?lang=%s&units=si");
        ReflectionTestUtils.setField(weatherService, "lang", "uk");
    }

    @Test
    public void getForecast() {
        List<String> forecast = weatherService.getForecast();
        assertEquals(2, forecast.size());
        assertTrue(forecast.get(0).length() > 40);
        assertTrue(forecast.get(1).length() > 40);
    }

    @Test
    public void getCondition() {
        String condition = weatherService.getCondition();
        assertNotNull(condition);
        assertTrue(condition.length() > 40);
    }
}