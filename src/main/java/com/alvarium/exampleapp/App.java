package com.alvarium.exampleapp;

import com.alvarium.SdkInfo;
import config.Reader;
import config.ReaderException;
import config.ReaderFactory;
import config.ReaderType;

public class App{
  public static void main( String[] args ) throws ReaderException {
    ReaderFactory factory = new ReaderFactory();
    Reader reader = factory.getReader(ReaderType.JSON);
    SdkInfo sdkInfo = reader.read("./src/main/resources/config.json");
  }

}
