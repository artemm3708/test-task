package com.eva;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args){
    String regex = "^.*[o].*$";
    System.out.println(encodeValue(regex));
  }

  private static String encodeValue(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

}
