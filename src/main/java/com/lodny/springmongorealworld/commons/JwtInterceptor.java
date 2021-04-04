package com.lodny.springmongorealworld.commons;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lodny.springmongorealworld.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

  private final String header = "Authorization";

  @Autowired
  JwtAuthTokenProvider jwt;

  @Autowired
  UserService service;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    System.out.println("> JwtInterceptor : preHaldle() : " + getTokenString(request.getHeader(header)));

    getTokenString(request.getHeader(header))
      .map(jwt::verifyJWT)
      .map(clames -> clames.getId())
      .map(service::findById)
      .ifPresent(user -> request.setAttribute("user", user));
    
    return true;
  }

  private Optional<String> getTokenString(String header) {
    
    if (header == null) return Optional.empty();    
    
    String[] split = header.split(" ");
    if (split.length < 2) {
        return Optional.empty();
    } else {
        return Optional.ofNullable(split[1]);
    }
  }

}
