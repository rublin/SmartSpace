package org.rublin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.rublin.to.weather.WeatherForecastDetails;
import org.rublin.to.weather.WeatherResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Weather controller receive JSON from darksky.net
 *
 * @author Ruslan Sheremet
 * @see JSONObject
 * @since 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weather.url}")
    private String url;

    @Value("${weather.token}")
    private String token;

    @Value("${weather.coordinates}")
    private String coordinates;

    @Value("${weather.lang}")
    private String lang;

    private final RestTemplate restTemplate;

    private final String[] helloArray = {"Доброго ранку.", "Привіт!", "Слава Україні!", "Героям слава!"};

    /**
     * Returns weather forecast for city in lang localization
     *
     * @return String forecast
     */
    public List<String> getForecast() {
        WeatherResponseDto forecast = getWeather();
        List<String> result = forecast.getDaily().getData().stream()
                .limit(2)
                .map(this::convertForecastResult)
                .collect(toList());
        log.info("Weather forecast {} got successfully", result.get(0));
        return result;
    }

    /**
     * Returns current weather for city in lang localization (from some observation station)
     *
     * @return String condition
     */
    public String getCondition() {
        int helloRandomPosition = ThreadLocalRandom.current().nextInt(0, helloArray.length);
        WeatherResponseDto weather = getWeather();
        String result = format("%s Поточна погода. " +
                        "Температура %s градусів цельсія. Відносна вологість %d. Швидкість вітру %d км/год. %s",
                helloArray[helloRandomPosition],
                fixTemperature(Math.round(weather.getCurrently().getTemperature())),
                Math.round(weather.getCurrently().getHumidity() * 100),
                Math.round(weather.getCurrently().getWindSpeed()),
                weather.getCurrently().getSummary());
        log.info("Current weather {} got successfully", result);
        return result;
    }

    private WeatherResponseDto getWeather() {
        String urlRequest = format(url, token, coordinates, lang);
        return restTemplate.getForObject(urlRequest, WeatherResponseDto.class);
    }

    private String convertForecastResult(WeatherForecastDetails details) {
        return format("%s Температура від %d до %d. Швидкість вітру %d.",
                details.getSummary(),
                Math.round(details.getTemperatureLow()),
                Math.round(details.getTemperatureHigh()),
                Math.round(details.getWindSpeed()));
    }

    private String fixTemperature(int temperature) {
        if (temperature == 10) {
            return "десять";
        } else if (temperature == 11) {
            return "одинадцять";
        } else if (temperature == 12) {
            return "дванадцять";
        } else if (temperature == 13) {
            return "тринадцять";
        } else if (temperature == 14) {
            return "чотирнадцять";
        } else if (temperature == 15) {
            return "п'ятнадцять";
        } else if (temperature == 16) {
            return "шістнадцять";
        } else if (temperature == 17) {
            return "сімнадцять";
        } else if (temperature == 18) {
            return "вісімнадцять";
        } else if (temperature == 19) {
            return "дев'ятнадцять";
        }
        return String.valueOf(temperature);
    }
}
