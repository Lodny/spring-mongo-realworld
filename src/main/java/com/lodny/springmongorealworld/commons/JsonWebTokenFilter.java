package com.lodny.springmongorealworld.commons;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

@WebFilter
public class JsonWebTokenFilter extends OncePerRequestFilter {

  private String header = "Authorization";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    System.out.println("> JsonWebTokenFilter : " + request.getHeader(header));

    filterChain.doFilter(request, response);
    
  }
  
}
