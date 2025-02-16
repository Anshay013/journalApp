package net.engineeringdigest.journalApp.utils;

import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private String SECRET_KEY = "TaK+HaV^uvCHEFsEVfypW#7g9^k*Z8$V";
    // this can be any complex, string of our choice (make it complex for security purpose)

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        // converted the secret key compatible with HMACSHA256
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token); // extract all the claims, i.e the Map<String, Object> claims we sent during generation of token
        return claims.getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // 1️⃣ Verify/ validate the token's signature with the secret key ( extra layer of security)
                .build()                     // 2️⃣ Build the parser instance, we know what parsing it, it basically scanning the JWT token details.
                .parseSignedClaims(token)    // 3️⃣ Parse the signed JWT and extract claims
                .getPayload();               // 4️⃣ Retrieve the JWT payload (claims)

    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // claims are sent into payload. Say we want to send some information into the payload
        // we use claims to do so.
        // Can be sent empty if we don't want to send anything. We can send userEmail as an extra field
        // which isn't in the payload Json file then send it with claims.
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        // pre-made builder for building jwt token by Jwts class under jsonwebtoken package
        return Jwts.builder()
                .claims(claims) // this is payload
                .subject(subject) // how do we identify this token e.g userName is our subject
                .header().empty().add("typ","JWT")
                // for header remember JWT structure there was alg, and typ, here we add the type is
                // JWT and by default alg is mentioned as H256.
                .and()
                .issuedAt(new Date(System.currentTimeMillis())) // time at which token is issued
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 60 minutes expiration time of token
                .signWith(getSigningKey()) // our secret key, that was in the structure of JWT under -> Signature
                // by default we are using HMACSHA256 for signing.
                .compact();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }


}