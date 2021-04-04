package com.lodny.springmongorealworld.commons;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.lodny.springmongorealworld.user.User;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public final class SECURITY {

  static public PasswordEncoder encoder = new BCryptPasswordEncoder();

  // @Value("${jwt.sessionTime}") 
  // private Long sessionTime;

  // public Security(@Value("${jwt.sessionTime}") int sessionTime) {
  //   this.sessionTime = sessionTime;
  // }

  static public String getEncodedPassword(String password) {
    return SECURITY.encoder.encode(password);
  }


  static public String generateToken(User user, Long sessionTime) {
  
    // header
    Map<String, Object> headers = new HashMap<>();
    headers.put("typ", "JWT");
    headers.put("alg", "HS256");

    // payload
    Map<String, Object> payloads = new HashMap<>();
    payloads.put("id", user.getId());
    payloads.put("email", user.getEmail());

    // expire
    Date ext = new Date();
    ext.setTime(ext.getTime() + sessionTime);

    // key
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); //or HS384 or HS512

    // generate token
    String token = Jwts.builder()
            .setHeader(headers)
            .setClaims(payloads)
            .setSubject("user")
            .setExpiration(ext)
            .signWith(key)
            .compact();
    
    System.out.println(">> Security : generateToken() : " + token);

    return token;
  }
}
