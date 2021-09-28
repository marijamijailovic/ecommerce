package com.demo.ecommerce.util;

public class Helpers {
  public static boolean validUUID(String uuid) {
    return uuid.matches(Constants.UUID_FORMAT);
  }
}
