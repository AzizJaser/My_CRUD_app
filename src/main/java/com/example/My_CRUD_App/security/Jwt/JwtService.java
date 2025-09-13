package com.example.My_CRUD_App.security.Jwt;

import com.example.My_CRUD_App.entity.User;
import com.example.My_CRUD_App.repository.UserRepository;
import com.example.My_CRUD_App.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMillis;

    @Autowired
    private UserRepository userRepository;


    public String generateToken(User user){

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRoles());

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expirationMillis);


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public boolean isValidToken(String token){
        // first check the email dose it exist or not ?
        String email = extractEmail(token);
        Date expiry = extractExpiration(token);
        if(!userRepository.existsByEmail(email)){
            throw new RuntimeException("Email dose not exsist !");
        }
        // second check if the token expired
        if(expiry.before(Date.from(Instant.now()))){
            throw new RuntimeException("Token expired !");
        }

        return true;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    private Key key(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
