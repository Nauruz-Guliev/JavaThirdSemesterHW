package ru.kpfu.itis.gnt.http.models;

public class WeatherModel {
    private String temperature;
    private String weatherDescription;
    private String relativeHumidity;
    private String precipitationLvl;

    public String getTemperature() {
        return temperature;
    }

    public String getDescriptiveTemperature() {
        return "Temperature: " + temperature + "C";
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return "Weather condition: " + weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setRelativeHumidity(String relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
    }

    public String getPrecipitationLvl() {
        return precipitationLvl;
    }

    public void setPrecipitationLvl(String precipitationLvl) {
        this.precipitationLvl = precipitationLvl;
    }

    @Override
    public String toString() {
        return "Weather Info{" +
                "temperature='" + temperature + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", relativeHumidity='" + relativeHumidity + '\'' +
                ", precipitationLvl='" + precipitationLvl + '\'' +
                '}';
    }
}
