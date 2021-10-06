package com.alvarium.exampleapp.observers;

public class ObserverException extends Exception {
  public ObserverException(String msg, Exception e) {
    super(msg, e);
  }
}
