package org.rublin.service;

import lombok.ToString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceTest {

    @Spy
    WeatherService weatherService = new WeatherService(new RestTemplate());

    @Before
    public void init() {
        ReflectionTestUtils.setField(weatherService, "token", "8d92d805fa64388c");
        ReflectionTestUtils.setField(weatherService, "city", "Kyiv");
        ReflectionTestUtils.setField(weatherService, "lang", "UA");
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

    @Test
    public void fahrenheitConverter() {
        String result = weatherService.convertFahrenheitToCelsius("Хмарно та вітряно. Максимум 51градуси Фаренгейта. Вітер Пн-Зх від 20 до 30 миль за годину.");
        assertEquals("Хмарно та вітряно. Максимум 10градуси цельсія. Вітер Пн-Зх від 20 до 30 миль за годину.", result);
    }
}