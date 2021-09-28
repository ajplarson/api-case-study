package com.example.demo.utils;

public class InvalidQueryException extends Exception {
  public InvalidQueryException(String errorMessage) {
    super(errorMessage);
  }
}
