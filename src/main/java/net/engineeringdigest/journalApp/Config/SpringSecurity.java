package net.engineeringdigest.journalApp.Config;

import net.engineeringdigest.journalApp.filter.JwtFilter;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //
@Profile("dev") // mentioning through profile that this class contains code that will run on dev server only
// for prod mention "prod"


public class SpringSecurity extends WebSecurityConfigurerAdapter {

    // WebSecurityConfigurerAdapter is a utility class in spring security framework that provides default configurations and allows customization of certain
    // features. By extending it, we can configure and customize Spring Security for our app. needs

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // it takes a password and encodes it into randomized string Hash.
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService) // contains userDetails that we have got from loadUserByUsername func in UserDetailsServiceImpl
                .passwordEncoder(passwordEncoder()); // process of encoding password (creating Hash) after fetching userDetails
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()  // tells spring security to start authorising the request.
                .antMatchers("/journal/**","/user/**").authenticated()// ** means its a wildcard it can contains 0, 1, 2 any character...
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable(); // writing this because we are getting 403 forbidden error while creating user
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // the code will also go to UsernamePasswordAuthenticationFilter, but filter won't
        // run because our own jwtFilter is applied before it.
        // we have add filter before and add filter after.. and so on. so after jwtFilter
        // there are many filter to run (also called filter Chaining -> back to back filter) --> caught by doFilterInternal method in SpringSecurity class


    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}