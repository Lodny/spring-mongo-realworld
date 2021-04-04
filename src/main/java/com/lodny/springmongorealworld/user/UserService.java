package com.lodny.springmongorealworld.user;

import com.lodny.springmongorealworld.exception.InvalidAuthenticationException;
import com.lodny.springmongorealworld.exception.InvalidRequestIDException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository repository;

  // public List<User> getAll() {
  // return repository.findAll();
  // }

  public User register(User user) {
    return repository.insert(user);
  }

  public User findById(String id) {
    return repository.findById(id).orElseThrow(() -> new InvalidRequestIDException());
  }

  public User findByEmail(String email) {
    return repository.findByEmail(email).orElseThrow(() -> new InvalidAuthenticationException());
  }

  public User findByUsername(String username) {
    return repository.findByUsername(username).orElseThrow(() -> new InvalidAuthenticationException());
  }
  
  public User update(User user) {
    return repository.save(user);
  }

}