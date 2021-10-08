package com.alvarium.exampleapp;

import com.alvarium.DefaultSdk;
import com.alvarium.Sdk;
import com.alvarium.SdkInfo;
import com.alvarium.exampleapp.observers.Channel;
import com.alvarium.annotators.Annotator;
import com.alvarium.annotators.AnnotatorException;
import com.alvarium.annotators.AnnotatorFactory;
import com.alvarium.contracts.AnnotationType;
import com.alvarium.streams.StreamException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import config.Reader;
import config.ReaderException;
import config.ReaderFactory;
import config.ReaderType;

public class App {
  public static void main(String[] args) throws ReaderException, AnnotatorException,
      StreamException {
    ReaderFactory readerFactory = new ReaderFactory();
    Reader reader = readerFactory.getReader(ReaderType.JSON);
    SdkInfo sdkInfo = reader.read("./src/main/resources/config.json");

    // init sdk dependencies
    final Annotator[] annotators = new Annotator[sdkInfo.getAnnotators().length];
    final AnnotatorFactory annotatorFactory = new AnnotatorFactory();
    for (int i = 0; i < annotators.length; i++) {
      final AnnotationType kind = sdkInfo.getAnnotators()[i];
      annotators[i] = annotatorFactory.getAnnotator(kind, sdkInfo);
    }

    final Logger logger = LogManager.getRootLogger();
    Configurator.setRootLevel(Level.DEBUG);

    final Sdk sdk = new DefaultSdk(annotators, sdkInfo, logger);

    // init channels and observers
    final Channel<SampleData> creationChannel = new Channel<SampleData>();
    final Channel<SampleData> mutationChannel = new Channel<SampleData>();
    final Channel<SampleData> transitionChannel = new Channel<SampleData>();

    sdk.close();
  }

}
