package net.engineeringdigest.journalApp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testSendMail(){
        redisTemplate.opsForValue().set("email", "adhayayna@gmail.com");

        Object email = redisTemplate.opsForValue().get("email");
        int a = 1;


        // opsForValue is an interface provided by redisTemplate, it has methods like set, get
        // By definition it should be String email = redisTemplate.opsForValue().get("email");
        // Remember Object class is the superclass of every class in Java so it can work.
    }


}
