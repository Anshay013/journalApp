package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service // good for readability and it contains @Component inside it
public class UserService {

    @Autowired
    UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public ResponseEntity<?> saveEntry(User user, String role){


        if(user != null && !user.getUserName().isEmpty() && !user.getPassword().isEmpty()){
            try {
                // if user with same userName was already present, collection won't save it(since userName has to be unique as per annotation)
                user.setPassword(passwordEncoder.encode(user.getPassword())); // encoding password before saving it
                user.setRoles(Arrays.asList(role)); // passing dummy role
                // and exception will be returned in catch block
                userRepository.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception ignored){
                return new ResponseEntity<>("same user name was found", HttpStatus.BAD_REQUEST);
            }
        }
        else return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

    }


    public void saveUpdatedUser(User user){
        if(user != null && !user.getUserName().isEmpty() && !user.getPassword().isEmpty()){
            try {
                // if user with same userName was already present, collection won't save it(since userName has to be unique as per annotation)
               // user.setPassword(passwordEncoder.encode(user.getPassword())); --> here we don't encode password
                user.setRoles(Arrays.asList("USER")); // passing dummy role
                // and exception will be returned in catch block
                userRepository.save(user);
                new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception ignored){
                new ResponseEntity<>("same user name was found", HttpStatus.BAD_REQUEST);
            }
        }
        else {
            new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    public ResponseEntity<List<User>> getAll(){
        List<User> myUsers = userRepository.findAll();
        if(!myUsers.isEmpty()){
            return new ResponseEntity<>(myUsers, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public ResponseEntity<?> getUserByName(String userName){
        try {
            User user = findByUserName(userName);
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
    }

    public ResponseEntity<?> updateUser(User user, String userName){
        User userInDb = findByUserName(userName);
        if(userInDb != null){
            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            saveEntry(userInDb, "USER"); //
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);


    }

    public void deleteEntryByName(String userName){
        try{
            userRepository.deleteByUserName(userName);
            new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            new ResponseEntity<>("user not found", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<String> detelAllUsers(){
        try{
            if(!userRepository.findAll().isEmpty()){
                userRepository.deleteAll();
                return new ResponseEntity<>("deleted all users",HttpStatus.ACCEPTED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        return null;
    }


    public ResponseEntity<User> updateEntry(ObjectId id, User user){
        User oldUser = userRepository.findById(id).orElse(null);


        if(oldUser != null) {
            oldUser.setUserName(!user.getUserName().isEmpty() ? user.getUserName() : oldUser.getUserName());
            oldUser.setPassword(!user.getPassword().isEmpty() ? user.getPassword() : oldUser.getPassword());
            saveUpdatedUser(oldUser);
            return new ResponseEntity<>(oldUser, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> deleteAllEntries(){
        List<User> myJournals = userRepository.findAll();
        if(!myJournals.isEmpty()) {
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


