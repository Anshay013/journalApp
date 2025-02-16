package net.engineeringdigest.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

 // Now response of diff. API's will have diff. JSON, which in turn needs to be de-serialized into different POJO's
    // Hence we are passing  Class<T> entitiyClass which is a POJO class for the specific API call
    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object o = redisTemplate.opsForValue().get(key); // storing it in object type
            ObjectMapper mapper = new ObjectMapper(); // create a ObjectMapper -> maps the returned object (jsonValue) to the POJO class sent as argument (below code)
            return mapper.readValue(o.toString(), entityClass); // returns a POJO class to that API, we have defined
        }
        catch (Exception e){
           System.out.println(e.getMessage());
           return null;
        }
    }

    public void set(String key, Object o, Long ttl) { // ttl -> time to live, recall AWS (expiry time in which cache data expires.
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(o); // convert the Object o to json Value via object Mapper
            // we can say objectMapper has two use cases -> serialize and de-serialize
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



}
