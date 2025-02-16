package net.engineeringdigest.journalApp.Controller;


import net.engineeringdigest.journalApp.api.response.JsonPlaceHolderResponse;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JsonPlaceHolderService;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private JsonPlaceHolderService placeHolderService;

    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        return userService.getAll();
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getByUserName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName(); // the one we typed in postman under basic authentication

        return userService.getUserByName(userName);
    }


// its a wrong practice to send username in pathParameter, hence we need to remove it.
//    @PutMapping("/{userName}")
//    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable String userName){
//        return userService.updateUser(user, userName);
//    }

    // spring security will help us to accomplish what the above updateUser fun does, without passing userName as path parameter

    @PutMapping //  the below fun will automatically catch the userName and password via which we hit this API -> power of spring security
    // through postman PUT -> Authorization -> basic Auth -> put username and password
    public ResponseEntity<?> updateUser(@RequestBody User user){
        // Any user which is authenticated its detail is stored in SecurityContextHolder
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName(); // we don't need password because we are damn sure the  flow will come here only and only if the user was already authenticated
        return userService.updateUser(user, userName);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.deleteEntryByName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteAll")
        public ResponseEntity<?> deleteAllUsers() {
            return userService.detelAllUsers();
        }


        @GetMapping
        public ResponseEntity<?> greeting() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            WeatherResponse weatherResponse = weatherService.getWeather("Bokaro");
            String greeting = "Hi " + authentication.getName();
            if(weatherResponse != null){
                greeting += "weather feels like  " + weatherResponse.getCurrent().getFeelslike();
            }
            else{
                greeting += " repsonse from server is NULL";
            }
            return new ResponseEntity<>(greeting, HttpStatus.OK);

        }

        @PostMapping("/external-post-request")
        public ResponseEntity<?> postingExternal(@RequestBody JsonPlaceHolderResponse response){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            ResponseEntity<?> postResponse = placeHolderService.createPost(response);
            String greeting = "Hi " + authentication.getName();
            if(postResponse != null){
                greeting += " The JSON data you posted is  " + postResponse.getBody();
            }
            else{
                greeting += " repsonse from server is NULL";
            }
            return new ResponseEntity<>(greeting, HttpStatus.OK);
        }


}
