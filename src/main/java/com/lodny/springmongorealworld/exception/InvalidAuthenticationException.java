package com.lodny.springmongorealworld.exception;

public class InvalidAuthenticationException extends RuntimeException {

  public InvalidAuthenticationException() {
    super("is invalid.");
  }  
}
