package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.ConfigJournalAppService;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

// it is un-authenticated Service
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigJournalAppService appService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/healthCheck") // i.e the below function gets mapped with this end point "/healthCheck"
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> createUser(@RequestBody User user){
        return userService.saveEntry(user, "USER"); //
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            System.out.println("Exception occurred while createAuthenticationToken " + e);
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create-admin")   // although its not a good practice to create admin from public controller, it just for testing purpose
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        return userService.saveEntry(user, "ADMIN");
    }

    @PostMapping("/create-api-config")
    public ResponseEntity<?> createAPiIConfig(@RequestBody ConfigJournalAppEntity appEntity){
        return appService.saveConfig(appEntity); // if there is no collection, a new collection will be created.
    }


}

/*
  Kamado, ka@123
  Naruto, na@123

  admin -
  admin, admin@123
  ansh_013, ansh@123
 */