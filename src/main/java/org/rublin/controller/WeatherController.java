package org.rublin.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import static org.rublin.util.Resources.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Weather controller receive JSON from api.wunderground.com
 *
 * @author Ruslan Sheremet
 * @see JSONObject
 * @since 1.0
 */
@Controller
public class WeatherController {
    private static final Logger LOG = getLogger(WeatherController.class);
    private static final String WEATHER_SERVICE = "http://api.wunderground.com/api/%s/%s/lang:%s/q/%s.json";

    /**
     * Returns weather forecast for city in lang localization
     *
     * @param city
     * @param lang
     * @return String forecast
     */
    public String getForecast(String city, String lang) {
        String url = String.format(WEATHER_SERVICE, WEATHER_TOKEN, "forecast", lang, city);
        JSONObject forecast = readJsonFromUrl(url).getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(0);
        /**
         * need to replace text using i18n
         */
        String result = "Прогноз погоди на " + forecast.getString("title") + ". " + forecast.getString("fcttext_metric");
        LOG.info("Weather forecast {} got successfully", result);
        return result;
    }

    /**
     * Returns current weather for city in lang localization (from some observation station)
     * @param city
     * @param lang
     * @return String condition
     */
    public String getCondition(String city, String lang) {
        String url = String.format(WEATHER_SERVICE, WEATHER_TOKEN, "conditions", lang, city);
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
        LOG.info("Current weather {} got successfully", result);
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
            LOG.error("Result from URL {} is not a JSON", url, e);
        } catch (IOException e) {
            LOG.error("Wrong source from URL {}", url, e);
        }
        return null;
    }

    private String fixTemperature(int temperature) {
        if (temperature == 11) {
            return "одинадцять";
        } else if (temperature == 12) {
            return "дванадцять";
        } else if (temperature == 13) {
            return "тринадцять";
        } else if (temperature == 14) {
            return "чотирнадцять";
        }
        return String.valueOf(temperature);
    }
}
