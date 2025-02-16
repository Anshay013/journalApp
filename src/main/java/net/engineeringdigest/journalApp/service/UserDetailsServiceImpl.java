package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// This UserDetailsService is provided by spring security
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    // here we are trying to return  UserDetails via  username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username); // find user by userName

        if (user != null) { // then we create a user
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName()) // set userName by getting the username from our user collection or .username(username)
                    .password(user.getPassword()) // similarly set password
                    .roles(user.getRoles().toArray(new String[0])) // set the roles
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
