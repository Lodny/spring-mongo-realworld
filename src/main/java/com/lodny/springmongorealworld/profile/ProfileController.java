package com.lodny.springmongorealworld.profile;

import javax.servlet.ServletRequest;

import com.lodny.springmongorealworld.commons.UTIL;
import com.lodny.springmongorealworld.exception.InvalidAuthenticationException;
import com.lodny.springmongorealworld.user.User;
import com.lodny.springmongorealworld.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/profiles")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

  @Autowired
  private UserService service;

  // profile
  @GetMapping("/{username}")
  public ResponseEntity<?> getUserByUsername(@PathVariable String username, ServletRequest request) {

    User user = (User)request.getAttribute("user");
    System.out.println("~~~~~~~~~~~~~~~~" + user == null );

    User profileUser = service.findByUsername(username);
    return new ResponseEntity<>(UTIL.jsonRoot("profile", profileUser.toProfileJSON(user)), HttpStatus.OK);
  }

  @PostMapping("/{username}/follow")
  public ResponseEntity<?> follow(@PathVariable String username, ServletRequest request) {

    User user = (User)request.getAttribute("user");
    if (null == user) throw new InvalidAuthenticationException();

    User newFollowing = service.findByUsername(username);

    System.out.println("> user : " + user.getUsername() + " : follow : " + newFollowing.getUsername());

    user.follow(newFollowing);
    service.update(user);

    return new ResponseEntity<>(UTIL.jsonRoot("profile", newFollowing.toProfileJSON(user)), HttpStatus.OK);
  }


}
