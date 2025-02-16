package net.engineeringdigest.journalApp.filter;

import net.engineeringdigest.journalApp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter  extends OncePerRequestFilter{ // If a request comes to the server it will filter it only once not everytime if a micro-service in the service calls
    // another microservice.
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    // this override method doFilterInternal will run as soon as addFilterBefore is called passed with the object of this class
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // request : the request form client
        // chain : filter chaining object
        String authorizationHeader = request.getHeader("Authorization"); // we get the authorization header from request -> in postman we have bearer token under Authorization
        // (in which we pass our JWT) from that JWT we extract the token (look up to postman under header section -> as soon as we paste the JWT we will have an authorization field
        // under header)

        String username = null;
        String jwt = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // Bearer followed by JWT (Bearer means who has this token (who possess it)
            jwt = authorizationHeader.substring(7); // the authorization header carries JWT so copy the authorization string from index 7 onwards to get JWT
            username = jwtUtil.extractUsername(jwt); // get the userName from JWT
        }
        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // load the userDetails by sending username extracted from JWT
            // this also validates the following - that the username extracted from JWT is a valid userNAme present in DB
            if (jwtUtil.validateToken(jwt)) { // if token is valid go in
                // now generate a new token of Authentication by sending userDetails
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // (userDetails.getAuthorities()): User roles and permissions (e.g., ROLE_USER, ROLE_ADMIN).
                // This tells Spring Security that the user is authenticated and authorized.

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            /*  Adds additional details (like IP address, session ID, etc.) to the authentication object. --> UsernamePasswordAuthenticationToken object
                buildDetails(request) extracts metadata from the current HTTP request.
                This helps in logging, auditing, and security monitoring.*/

                SecurityContextHolder.getContext().setAuthentication(auth);
                // remember all the endpoint funs.
                // Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); we get here when user is authenticated
                // so we are getting the authToken here and accessing it in the endpoint fun. Before we used to directly call getAuthentication()
                // because it was via HttpBasic(), but now JWT auth is needed to do this we have to set a filter to validate it.
            }
        }
        response.addHeader("admin", "admin"); // we can also add headers to response.
        // note this filter will be called before the control goes to endpoint fun (i.e PublicController)

        chain.doFilter(request, response); // in chain we sent the request and response ahead, for the filters that will come after the jwtFilter
        // so that now this JWT validation doesn't need to be done again and again in the same server.
    }


    // flow ->
    /* 1. control comes to configure fun (the one with HttpSecurity, check if the end point is secured via spring security)
       2. doFilterInternal is called filter is set, from authorization header JWT is extracted along with userName (via the extracted jwt)
       3. authenticationToken is generated and authentication is set SecurityContextHolder.
       4. Now in controller class not public that is protected by spring security.
        from  Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); userName is extracted.
       5. Now with this updated SecurityContextHolder, we get to the endpoint we called and extract that userName and proceed with logic.

     The above is good when the token is already generated, but lets say we want to generate a token i.e login then what is the flow.
     The flow will be same but here in jwtFiler header, token, userName all will be null (obvious because token was never there) and the control
     // will go to endpoint, where getAuthentication() will call its default auth flow get credentials via loadUserByUsername flow and generate a new token

     // when the token is generated the flow isn't guided to loadUserByUsername but for validation we extract userName form jwt and compare it
     // with loadUserByUsername, if the userName is real or not.
    *
    * */
}