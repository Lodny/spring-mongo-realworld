package com.lodny.springmongorealworld.user;

import java.util.Optional;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import com.lodny.springmongorealworld.commons.JwtAuthTokenProvider;
import com.lodny.springmongorealworld.commons.UTIL;
import com.lodny.springmongorealworld.exception.ErrorDto;
import com.lodny.springmongorealworld.exception.InvalidAuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

  @Autowired
  private UserService service;

  @Autowired
  private JwtAuthTokenProvider jwt;

  @Value("${image.default}")
  private String default_image;

  // register
  @PostMapping("/users")
  public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDto dto, BindingResult result) {

    User user = new User(dto);
    user.setImage(default_image);
    user = service.register(user);

    Optional<String> token = jwt.createJwtAuthToken(user.getId(), user.getEmail());
    return new ResponseEntity<>(UTIL.jsonRoot("user", user.toAuthJSON(token)), HttpStatus.CREATED);
  }
  
  // login
  @PostMapping("/users/login")
  public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto dto, BindingResult result) {

    User user = null;
    try {
      user = service.findByEmail(dto.getEmail());
      if (!user.checkPassword(dto.getPassword()))
        throw new InvalidAuthenticationException();
    } catch (InvalidAuthenticationException e) {
      ErrorDto error = new ErrorDto();
      error.add("email or password", e.getMessage());
      return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    Optional<String> token = jwt.createJwtAuthToken(user.getId(), user.getEmail());
    return new ResponseEntity<>(UTIL.jsonRoot("user", user.toAuthJSON(token)), HttpStatus.OK);
  }

  // update
  @PutMapping("/user")
  public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateDto dto, BindingResult result, ServletRequest request) {

    if (null == request.getAttribute("user"))
      throw new InvalidAuthenticationException();
    
    User user = (User)request.getAttribute("user");
    user.update(dto);
    service.update(user);

    Optional<String> token = jwt.createJwtAuthToken(user.getId(), user.getEmail());
    
    // User user = null;
    // try {
    //   user = service.findById(dto.getId());
    //   user.update(dto);
    //   service.update(user);
    // } catch (InvalidRequestIDException e) {
    //   ErrorDto error = new ErrorDto();
    //   error.add("login session", e.getMessage());
    //   return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    // }
    
    return new ResponseEntity<>(UTIL.jsonRoot("user", user.toAuthJSON(token)), HttpStatus.OK);
  }
  
}
