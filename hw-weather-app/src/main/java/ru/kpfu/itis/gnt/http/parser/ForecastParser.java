package ru.kpfu.itis.gnt.http.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.kpfu.itis.gnt.http.models.WeatherModel;

public class ForecastParser {
    private WeatherModel weatherModel;
    private final String responseBody;

    public ForecastParser(String responseBody) {
        this.responseBody = responseBody;
        parse();
    }

    public void parse() {
        weatherModel = new WeatherModel();
        JsonArray rootElement = new JsonParser().parse(responseBody).getAsJsonArray();
        for (JsonElement jElement : rootElement) {
            JsonObject weatherObj = jElement.getAsJsonObject();
            weatherModel.setTemperature(String.valueOf(weatherObj.getAsJsonObject("Temperature").getAsJsonObject("Metric").get("Value").getAsDouble()));
            weatherModel.setWeatherDescription(weatherObj.get("WeatherText").getAsString());
            weatherModel.setRelativeHumidity(String.valueOf(weatherObj.get("RelativeHumidity").getAsInt()));
            weatherModel.setPrecipitationLvl(String.valueOf(weatherObj.getAsJsonObject("PrecipitationSummary").getAsJsonObject("Precipitation").getAsJsonObject("Metric").get("Value").getAsDouble()));
        }
    }

    public WeatherModel getWeatherInfo() {
        return weatherModel;
    }
}
