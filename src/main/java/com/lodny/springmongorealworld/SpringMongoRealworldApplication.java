package com.lodny.springmongorealworld;

import com.lodny.springmongorealworld.commons.JwtAuthTokenProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringMongoRealworldApplication {

  @Value("${jwt.secret}")
  private String secret;
  @Value("${jwt.expiredMins}")
  private int expiredMins;

	public static void main(String[] args) {
		SpringApplication.run(SpringMongoRealworldApplication.class, args);
	}

  @Bean
  public JwtAuthTokenProvider jwtProvider() {
      return new JwtAuthTokenProvider(secret, expiredMins);
  }
}
