package com.lodny.springmongorealworld;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.lodny.springmongorealworld.commons.JwtAuthTokenProvider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@SpringBootApplication
public class SpringMongoRealworldApplication {

  @Value("${jwt.secret}")
  private String secret;
  @Value("${jwt.expiredMins}")
  private int expiredMins;

	public static void main(String[] args) {
		SpringApplication.run(SpringMongoRealworldApplication.class, args);

    List<String> list = Arrays.asList("a", "b", "c", null);
        String result = list.stream()
          .map((@NonNull var x) -> x.toUpperCase())
          .collect(Collectors.joining(","));
        
          System.out.println(result);
	}

  @Bean
  public JwtAuthTokenProvider jwtProvider() {
      return new JwtAuthTokenProvider(secret, expiredMins);
  }
}
