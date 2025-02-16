package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.Cache.AppCache;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private  String apiKey;
    private static String API;

    @Autowired
    private RestTemplate restTemplate; // injected via @Autowired, implementation written in Journal Application class

    @Autowired
    AppCache appCache;

    // since its a free plan we can only make http request, no TLS, in flight encryption
    // New%20York --> %20 is nothing but space -> New York

    @Autowired
    RedisService redisService;

    public WeatherResponse getWeather(String city){
        WeatherResponse cachedWeatherResponse = redisService.get("weather_of_" + city, WeatherResponse.class); // we are already getting body from here
        if(cachedWeatherResponse != null){
            return cachedWeatherResponse;
        }
        else{
            API = appCache.getAPP_CACHE().get("weather_api");
            String finalAPI = API.replace("CITY", city).replace("API_KEY", apiKey); // never do it like this there's a proper way to call it
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            // the response will be in the form of JSON which will be deserialized by the class we passed
            HttpStatus status = response.getStatusCode();
            WeatherResponse body = response.getBody(); // after the response is deserialized return it.

            // note through responseEntity we send Http code for the request made with the body we want to send.
            // We also catch a response from a responseEntity

            // the response we get is in the form of JSON, then make a POJO class for that JSON response for de-serializing it and pass it --> WeatherResponse.class

            if(body != null){
                // save to cache for further calls
                redisService.set("weather_of_" + city, body, 300L);
            }
            switch(status){
                case OK:
                    return body;

                case NOT_FOUND:
                    return null;

                default:
                    System.out.println("unhandled Status " + status);
            }

        }
        return null;
    }


}
