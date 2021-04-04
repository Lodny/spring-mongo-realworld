package com.lodny.springmongorealworld.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName("user")
public class ErrorDto {
  private Map<String, List<String>> errors;

  public ErrorDto() {
    errors = new HashMap<>();
  }

  public void add(String key, String message) {
    List<String> errorList = errors.get(key);

    if (errorList == null) {
      errorList = new ArrayList<>();
      errors.put(key, errorList);
    }

    errorList.add(message);
  }
}
