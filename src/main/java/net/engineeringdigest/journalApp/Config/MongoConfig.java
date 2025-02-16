package net.engineeringdigest.journalApp.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class MongoConfig {

    @Bean(name = "platformTransactionManager")
    public PlatformTransactionManager platformTransactionManager(MongoDatabaseFactory dbFactory){
        return new MongoTransactionManager(dbFactory);
    }
    // MongoTransactionManager implements PlatformTransactionManager and has its own share of differences

    @Bean(name = "mongoTransactionManager")
    MongoTransactionManager mongoTransactionManager(MongoDatabaseFactory dbFactory){
        return new MongoTransactionManager(dbFactory);
    }
    // note here we gave an argument dbFactory, what spring does is, it injects the object of the argument since the func is inside @Bean

}


