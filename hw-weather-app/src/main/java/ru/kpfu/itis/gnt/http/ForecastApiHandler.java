package ru.kpfu.itis.gnt.http;

import ru.kpfu.itis.gnt.exceptions.WeatherRequestException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ForecastApiHandler {
    private static final String URL_FORECAST = "http://dataservice.accuweather.com/currentconditions/v1/";
    private static final String API_KEY =  "iVYNpu7EtJ9spUeUApWTszQJCYqQUXhY";
    private static final String DETAILS_OPTION = "&details=true";


    public static String getHttpResponse(String cityId) throws WeatherRequestException {
        URL url = createUrl(cityId);
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(url.openStream())))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException ex) {
            throw new WeatherRequestException("Unable to retrieve http response due to " + ex.getMessage());
        }
        if (response.isEmpty() || response.toString().isBlank()) {
            throw new WeatherRequestException("Unable to get HTTP response. Probably your API-key is invalid.");
        }
        return response.toString();
    }



    private static URL createUrl(String cityId) throws WeatherRequestException {
        try {
            return new URL(URL_FORECAST
                    + cityId
                    + "?apikey="
                    + API_KEY
                    + DETAILS_OPTION
            );
        } catch (MalformedURLException ex) {
            throw new WeatherRequestException("Unable to create URL due to: " + ex.getMessage());
        }
    }
}
