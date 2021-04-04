package com.lodny.springmongorealworld.user;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName(value = "user")
public class UserUpdateDto {
  
  // @Size(max=20, message = "max length is 20")
  private String username;

  // @Size(max=20, message = "max length is 20")
  // @Email
  private String email;

  // @Size(min=3, message = "min length is 3")
  private String password;

  private String bio;
  private String image;


  /*
  // @Size(max=20, message = "max length is 20")
  private Optional<String> username;

  // @Size(max=20, message = "max length is 20")
  // @Email
  private Optional<String> email;

  // @Size(min=3, message = "min length is 3")
  private Optional<String> password;

  private Optional<String> bio;
  private Optional<String> image;
  */
}
