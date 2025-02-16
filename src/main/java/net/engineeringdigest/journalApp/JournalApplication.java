package net.engineeringdigest.journalApp;

import net.engineeringdigest.journalApp.api.response.JsonPlaceHolderResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication // this annotation will only be on the main class _. jisme main method hoga
//@EnableTransactionManagement // it says to give me the methods that have @Transactional annotation with it
@EnableScheduling // tell spring boot that we have methods like scheduling, find all method with @Scheduled on it
public class JournalApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JournalApplication.class, args);
        ConfigurableEnvironment environment = context.getEnvironment();
        System.out.println(environment.getActiveProfiles()[0]); // .getActive profiles is an array which which help fetch what is current active profiloe set, dev, prod etc.
        // it is an array because we can give multiple profiles together
    }


    // note @SpringBootApplication -> has 3 annotaion within it
    // 1. @EnableAutoConfiguration  2. @ComponentScan  3. @Configuration

    // below we are writing configurations that means we can create beans (using below methods) in this class -

//    @Bean // spring will find which bean is implementing PlatformTransactionManager -> can have any name add, flana, dhamkana
//    public PlatformTransactionManager add(MongoDatabaseFactory dbFactory){
//        return new MongoTransactionManager(dbFactory);
//    }

//    @Bean
//    public PlatformTransactionManager faaddlana(MongoDatabaseFactory dbFactory){
//        return new MongoTransactionManager(dbFactory);
//    }

    // the above is the only thing that we need to define spring will automatically detect where the PlatformTransactionManager is (the manager is itself manage every session
    // and transaction, we don't need to handle anything)


    // for the above bean function we can make a diff config class annotated with @Configuration and put the bean function in there


// controller --> service --> repository (this repo interacts with DB and runs query)
//
//    @Bean
//    public RestTemplate restTemplate(){ // this instance is created in Application file and can be injected anywhere
//        return new RestTemplate();
//   }
//
//   @Bean
//    public JsonPlaceHolderResponse placeHolderResponse(){
//        return new JsonPlaceHolderResponse();
//   }


}

// PlatformTransactionalManager -> interface (the interface which actuality does commit and rollback on db operations)
// MongoTransactionalManager -> above interface implementation