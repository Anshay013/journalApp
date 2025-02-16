package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String username);
    //    The function works because of JPA's Query Method Mechanism. The findByUserName method follows the naming conventions recognized by Spring Data.
//    findBy - Indicates that this method will perform a query to find an entity.
//
    void deleteByUserName(String username);

    @Query("{'email' : {$regex: '?0', $options: 'i'}}") // give all users whose email is valid by the pattern
    List<User> findByEmailPattern(String pattern);
    // ?0 -> placeHolder for pattern.

    // I would encourage to use new Query() to do this, because that is more developer readable and friendly to handle.
}