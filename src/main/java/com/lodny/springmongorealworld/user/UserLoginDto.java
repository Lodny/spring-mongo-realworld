package com.lodny.springmongorealworld.user;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName(value = "user")
public class UserLoginDto {

  @NotBlank(message = "can't be blank")
  // @Size(max=20, message = "max length is 20")
  // @Email
  private String email;

  @NotBlank(message = "can't be blank")
  private String password;
}
