package org.rublin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.rublin.to.weather.WeatherForecastResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Weather controller receive JSON from api.wunderground.com
 *
 * @author Ruslan Sheremet
 * @see JSONObject
 * @since 1.0
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weather.token}")
    private String token;

    @Value("${weather.city}")
    private String city;

    @Value("${weather.lang}")
    private String lang;

    private final RestTemplate restTemplate;

    private final String[] helloArray = {"Доброго ранку.", "Привіт!", "Слава Україні!", "Героям слава!"};

    private static final String WEATHER_SERVICE = "http://api.wunderground.com/api/%s/%s/lang:%s/q/%s.json";
    private static final Pattern PATTERN = Pattern.compile("1\\d");

    /**
     * Returns weather forecast for city in lang localization
     *
     * @return String forecast
     */
    public List<String> getForecast() {
        String url = format(WEATHER_SERVICE, token, "forecast", lang, city);
        WeatherForecastResponseDto forecast = restTemplate.getForObject(url, WeatherForecastResponseDto.class);
        List<String> result = forecast.forecastDays().stream()
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
        String url = format(WEATHER_SERVICE, token, "conditions", lang, city);
        JSONObject current = readJsonFromUrl(url).getJSONObject("current_observation");
        String weather = current.getString("weather");
        int temp = current.getInt("temp_c");
        int dewpoint = current.getInt("dewpoint_c");
        int wind_speed = current.getInt("wind_kph");
        String humidity = current.getString("relative_humidity");
        /*
          need to replace text using i18n
         */
        String result = format("%s Поточна погода (Київська метеостанція). " +
                        "Температура %s градусів цельсія. Точка роси %d. Відносна вологість %s. Швидкість вітру %d км/год. %s",
                helloArray[helloRandomPosition],
                fixTemperature(temp),
                dewpoint,
                humidity,
                wind_speed,
                weather);
        log.info("Current weather {} got successfully", result);
        return result;
    }

    private String convertForecastResult(WeatherForecastResponseDto.ForecastDay forecastDay) {
        return format("%s. %s", forecastDay.getTitle(), StringUtils.isEmpty(forecastDay.getFcttext_metric())
                ? convertFahrenheitToCelsius(forecastDay.getFcttext())
                : forecastDay.getFcttext_metric());
    }

    /**
     * Replace fahrenheit to celsius in string like this:
     * Хмарно та вітряно. Максимум 51градуси Фаренгейта. Вітер Пн-Зх від 20 до 30 миль за годину.
     *
     * @param fahrenheitForecast
     * @return
     */
    String convertFahrenheitToCelsius(String fahrenheitForecast) {
        Pattern pattern = Pattern.compile("(\\d+)градуси Фаренгейта");
        Matcher matcher = pattern.matcher(fahrenheitForecast);
        if (matcher.find()) {
            String fahrenheit = matcher.group(1);
            int celsius = (Integer.parseInt(fahrenheit) - 32) * 5 / 9;
            String result = fahrenheitForecast
                    .replace(fahrenheit, String.valueOf(celsius))
                    .replace("градуси Фаренгейта", "градуси цельсія");
            return result;
        }
        return fahrenheitForecast;
    }

    /**
     * Read JSON from URL
     *
     * @param url
     * @return JSONObject
     * @see JSONObject
     */
    private JSONObject readJsonFromUrl(String url) {
        try (InputStream stream = new URL(url).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
            StringBuffer jsonText = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            return new JSONObject(jsonText.toString());
        } catch (MalformedURLException e) {
            log.error("Result from URL {} is not a JSON", url, e);
        } catch (IOException e) {
            log.error("Wrong source from URL {}", url, e);
        }
        return null;
    }

    private String fixTemperature(String origin) {
        String result = origin;
        Matcher matcher = PATTERN.matcher(origin);
        if (matcher.find()) {
            String number = matcher.group();
            result = origin.replaceAll(number, fixTemperature(Integer.valueOf(number)).concat(" "));
        }

        return result;
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
