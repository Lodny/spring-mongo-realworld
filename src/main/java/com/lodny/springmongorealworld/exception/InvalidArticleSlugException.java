package com.lodny.springmongorealworld.exception;

public class InvalidArticleSlugException extends RuntimeException {
  
  public InvalidArticleSlugException() {
    super("can not find.");
  }
}
