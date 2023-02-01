package ru.kpfu.itis.gnt.http;

import ru.kpfu.itis.gnt.exceptions.WeatherRequestException;
import ru.kpfu.itis.gnt.http.models.WeatherModel;
import ru.kpfu.itis.gnt.http.parser.ForecastParser;

import java.util.HashMap;


public class WeatherInfo {

    public HashMap<String, String> getCitiesMap() {
        return citiesMap;
    }
    private final HashMap<String, String> citiesMap;

    public WeatherInfo() {
        this.citiesMap = new HashMap<String, String>();
        this.initializeCities();
    }

    public void initializeCities() {
        citiesMap.put("Kazan", "295954");
        citiesMap.put("Moscow", "294021");
        citiesMap.put("Saint Petersburg", "295212");
        citiesMap.put("Dubai", "323091");
    }

    public WeatherModel getCityWeather(String city) throws WeatherRequestException {
        return new ForecastParser(
                ForecastApiHandler.getHttpResponse(citiesMap.get(city))
        ).getWeatherInfo();
    }
}
