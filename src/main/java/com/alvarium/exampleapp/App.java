package com.alvarium.exampleapp;

import com.alvarium.SdkInfo;
import com.alvarium.exampleapp.observers.Channel;

import config.Reader;
import config.ReaderException;
import config.ReaderFactory;
import config.ReaderType;

public class App{
  public static void main( String[] args ) throws ReaderException {
    ReaderFactory factory = new ReaderFactory();
    Reader reader = factory.getReader(ReaderType.JSON);
    SdkInfo sdkInfo = reader.read("./src/main/resources/config.json");

    final Channel<SampleData> createChannel = new Channel<SampleData>();
    final Channel<SampleData> mutateChannel = new Channel<SampleData>();
    final Channel<SampleData> transitChannel = new Channel<SampleData>();

  }

}
