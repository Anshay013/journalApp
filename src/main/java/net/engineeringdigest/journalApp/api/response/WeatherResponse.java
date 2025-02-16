package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// instead of getter and setter we could use lombok @Data for getter and setter
@Getter
@Setter
public class WeatherResponse {

    private Current current;
    @Getter
    @Setter
    public static class Current{

        public int temperature;


        @JsonProperty("weather_descriptions") // it indicates what original name json response had
        public List<String> weatherDescriptions;

        public int feelslike;

    }




}
