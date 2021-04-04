package com.lodny.springmongorealworld.commons;

import java.util.HashMap;
import java.util.Map;


public final class UTIL {

  static public Map<String, Object> jsonRoot(String name, Object obj) {
    Map<String, Object> map = new HashMap<>();
    map.put(name, obj);
    return map;

    // return (new HashMap<String, Object>() {
    // {
    // put(name, obj);
    // }
    // });
  }

  //토큰 검증



  // static public String encodedPassword(String password) {
  //   return (new BCryptPasswordEncoder()).encode(password);
  // }

  // static public Boolean confirmPassword(String password, String encodedPassword) {
  //   return (new BCryptPasswordEncoder()).matches(password, encodedPassword);
  // }
}
