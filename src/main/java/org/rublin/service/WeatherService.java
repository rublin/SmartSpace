package org.rublin.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Weather controller receive JSON from api.wunderground.com
 *
 * @author Ruslan Sheremet
 * @see JSONObject
 * @since 1.0
 */

@Slf4j
@Service
public class WeatherService {

    @Value("${weather.token}")
    private String token;
    
    private static final String WEATHER_SERVICE = "http://api.wunderground.com/api/%s/%s/lang:%s/q/%s.json";
    private static final Pattern PATTERN = Pattern.compile("1\\d");

    /**
     * Returns weather forecast for city in lang localization
     *
     * @param city
     * @param lang
     * @return String forecast
     */
    public String getForecast(String city, String lang) {
        String url = String.format(WEATHER_SERVICE, token, "forecast", lang, city);
        JSONObject forecast = readJsonFromUrl(url).getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(0);

        /**
         * need to replace text using i18n
         */
        String result = "Прогноз погоди на " + forecast.getString("title") + ". " + fixTemperature(forecast.getString("fcttext_metric"));
        log.info("Weather forecast {} got successfully", result);
        return result;
    }

    /**
     * Returns current weather for city in lang localization (from some observation station)
     * @param city
     * @param lang
     * @return String condition
     */
    public String getCondition(String city, String lang) {
        String url = String.format(WEATHER_SERVICE, token, "conditions", lang, city);
        JSONObject current = readJsonFromUrl(url).getJSONObject("current_observation");
        String weather = current.getString("weather");
        int temp = current.getInt("temp_c");
        int dewpoint = current.getInt("dewpoint_c");
        int wind_speed = current.getInt("wind_kph");
        String humidity = current.getString("relative_humidity");
        /**
         * need to replace text using i18n
         */
        String result = String.format("Поточна погода (Київська метеостанція). Температура %s градусів цельсія. Точка роси %d. Відносна вологість %s. Швидкість вітру %d км/год. %s", fixTemperature(temp), dewpoint, humidity, wind_speed, weather);
        log.info("Current weather {} got successfully", result);
        return result;
    }

    /**
     * Read JSON from URL
     * @param url
     * @see JSONObject
     * @return JSONObject
     */
    private JSONObject readJsonFromUrl(String url) {
        try (InputStream stream = new URL(url).openStream()){
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
