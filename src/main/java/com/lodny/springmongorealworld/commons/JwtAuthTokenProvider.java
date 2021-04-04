package com.lodny.springmongorealworld.commons;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import com.lodny.springmongorealworld.exception.CustomJwtRuntimeException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

public class JwtAuthTokenProvider {
 
  private final Key key;
  private final int expiredMins;

  private static final String AUTHORITIES_KEY = "email";


  public JwtAuthTokenProvider(String secret, int expiredMins) {
      this.key = Keys.hmacShaKeyFor(secret.getBytes());
      this.expiredMins = expiredMins;
  }

  // expired : seconds
  public Optional<String> createJwtAuthToken(String id, String email) {
  // public String createJwtAuthToken(String id, String role) {
    // expire
    Date expiredDate = new Date();
    expiredDate.setTime(expiredDate.getTime() + this.expiredMins * 1000);

    String token = Jwts.builder()
            // .setSubject(id)
            .setId(id)
            .claim(AUTHORITIES_KEY, email)
            .signWith(this.key, SignatureAlgorithm.HS256)
            .setExpiration(expiredDate)
            .compact();

    return Optional.ofNullable(token);
    // return token;
  }  

  public Claims verifyJWT(String jwt) {

    try {
      return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
    } catch (MalformedJwtException e) {
      System.out.println("> JwtInterceptor : verifyJWT() : Malformed");
      throw new CustomJwtRuntimeException();
    } catch (ExpiredJwtException e) {
      System.out.println("> JwtInterceptor : verifyJWT() : Expired");
      throw new CustomJwtRuntimeException();
    } catch (UnsupportedJwtException e) {
      System.out.println("> JwtInterceptor : verifyJWT() : Unsupported");
      throw new CustomJwtRuntimeException();
    } catch (IllegalArgumentException e) {
      System.out.println("> JwtInterceptor : verifyJWT() : Invalid");
      throw new CustomJwtRuntimeException();
    } catch (SecurityException e) {
      System.out.println("> JwtInterceptor : verifyJWT() : Invalid JWT signature.");
      throw new CustomJwtRuntimeException();
    }
  }  
}
