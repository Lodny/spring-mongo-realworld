package com.lodny.springmongorealworld.user;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName(value = "user")
public class UserRegisterDto {

  // @NotNull(message = "none")
  @NotBlank(message = "can't be blank") // " " => X
  // @Size(max=20, message = "max length is 20")
  private String username;

  // @NotNull(message = "none")
  @NotBlank(message = "can't be blank")
  // @Size(max=20, message = "max length is 20")
  // @Email
  private String email;

  // @NotNull(message = "none")
  @NotBlank(message = "can't be blank")
  // @Size(min=3, message = "min length is 3")
  private String password;
}
