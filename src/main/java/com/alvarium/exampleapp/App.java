/*******************************************************************************
 * Copyright 2021 Dell Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package com.alvarium.exampleapp;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.alvarium.DefaultSdk;
import com.alvarium.Sdk;
import com.alvarium.SdkInfo;
import com.alvarium.exampleapp.observers.Channel;
import com.alvarium.exampleapp.observers.CreationObserver;
import com.alvarium.sign.KeyInfo;
import com.alvarium.sign.SignException;
import com.alvarium.exampleapp.observers.TransitionObserver;
import com.alvarium.exampleapp.observers.MutationObserver;
import com.alvarium.annotators.Annotator;
import com.alvarium.annotators.AnnotatorException;
import com.alvarium.annotators.AnnotatorFactory;
import com.alvarium.contracts.AnnotationType;
import com.alvarium.streams.StreamException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import sun.misc.Signal;

import config.Reader;
import config.ReaderException;
import config.ReaderFactory;
import config.ReaderType;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.exceptions.Exceptions;

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

    final Observer<SampleData> creationObserver = new CreationObserver(sdk, mutationChannel);
    creationChannel.registerObserver(creationObserver);
    final Observer <SampleData> transitionObserver = new TransitionObserver(sdk);
    transitionChannel.registerObserver(transitionObserver);

    // Get keyInfo to generate new sample data
    final KeyInfo keyInfo = sdkInfo.getSignature().getPrivateKey();
    

    final Observer<SampleData> mutationObserver = new MutationObserver(
      sdk, 
      keyInfo, 
      transitionChannel
    );
    mutationChannel.registerObserver(mutationObserver);

    final Timer timer = new Timer();
    final TimerTask generateData = new TimerTask() {
      @Override
      public void run() {
        try {
          final SampleData data = new SampleData(keyInfo);
          creationChannel.publish(data);
        } catch(SignException e ) {
          Exceptions.propagate(e);
        } catch(IOException e) {
          Exceptions.propagate(e);
        }
      }
    };
    
    // Data creation loop which will run every 1000ms 
    timer.scheduleAtFixedRate(generateData, 0, 1000);

    // Calls sdk.close() and closes all channels
    final Runnable dispose = new Runnable() {
      @Override
      public void run() {
        try {
          sdk.close();
          creationChannel.close();
          mutationChannel.close();
          transitionChannel.close();
          System.exit(0);
        } catch(StreamException e) {
          Exceptions.propagate(e);
          System.out.println("Could not shutdown gracefully");
        }
      }
    };

    // Catch SIGINT to shutdown gracefully
    Signal.handle(new Signal("INT"), signal -> dispose.run());

    // Catch SIGTERM to shutdown gracefully
    Signal.handle(new Signal("TERM"), signal -> dispose.run());

  }
}
