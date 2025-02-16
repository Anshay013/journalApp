package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

// How here what if Query is complex, spring won't be able to recognise it via the fun name. So for this we use Queries.
// e.g This class has functions to interact with users collection with complex queries ?

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;
    // A class provided by Spring data MongoDb to interact with collections.
    // This class has implementation to do so. Given it is autoConfigured via Spring boot


    public List<User> getUserForSA1(String userName){
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(userName));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true).and("age")
                .is(20)); // query where field is true and and age is 20. gte -> greater than equal to

        // Also a thing to note that since we have added query.addCriteria two times it will take intersection of both like "AND"


//        // Now adding OR operator to both criteria addition. adding comma "," between Criteria where clause.
//        Criteria criteria = new Criteria();
//        query.addCriteria(criteria.orOperator( Criteria.where("userName").is(userName), Criteria.where("age").gte("20") ));
//
//        // By the above method we can also add "AND" operator
//        Criteria criteria = new Criteria();
//        query.addCriteria(criteria.andOperator( Criteria.where("userName").is(userName), Criteria.where("age").gte("20") ));

        // Checking if the email is valid, by regular expression and sentiment analysis is true-
//        query.addCriteria(Criteria.where("email").regex("[a-zA-Z!@#\\$%\\^&\\*\\(\\)_\\+\\-=\\[\\]\\{\\};:'\",<>\\./?\\\\|`~]\n"));
//        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        List<User> users = mongoTemplate.find(query, User.class); // ORM compatible just providing class not collection
        // tell mongoTemplate to perform the query to collection User.
        return users;
    }

    // The query takes Key value pair, Key = "userName" and value = userName
    // key -> must be fields of te collection we want to find and send its corresponding value




// lets schedule task -

    public List<User> getUserForSA(String userName){
        Query query = new Query();
        query.addCriteria(Criteria.where("userName").is(userName));

        return mongoTemplate.find(query, User.class); // return as list
    }



}

