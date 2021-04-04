package com.lodny.springmongorealworld.exception;

public class CustomJwtRuntimeException extends RuntimeException {
  
  public CustomJwtRuntimeException() {
    super("Json Web Token is invalid.");
  }
}
