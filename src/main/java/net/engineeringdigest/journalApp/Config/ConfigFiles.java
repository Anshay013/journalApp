package net.engineeringdigest.journalApp.Config;

import net.engineeringdigest.journalApp.api.response.JsonPlaceHolderResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConfigFiles {

    @Bean
    public RestTemplate restTemplate(){ // this instance is created in Application file and can be injected anywhere
        return new RestTemplate();
    }

    @Bean
    public JsonPlaceHolderResponse placeHolderResponse(){
        return new JsonPlaceHolderResponse();
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
         RedisTemplate redisTemplate = new RedisTemplate<>();
         redisTemplate.setConnectionFactory(redisConnectionFactory);
         // setting up the serializer
        // We set serializer to both -
        // 1. Key
        // 2. Value
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // we want both  key and value to be stored as string
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    // this RedisConnectionFactory is an interface which is used to make connection from redis server and manage the connection.
    // open it for more details, it has underlying functions which does that

    // setting up Serializer means it has both functions inside it serialize and de-serialize. (there is nothing like setting up de-serializer, don't go by the name)
     // this StringRedisSerializer set serializer and De-serializer in StandardCharsets.UTF_8
    // note there are many diff. serializer types -> StringRedisSerializer is one of them.


}
