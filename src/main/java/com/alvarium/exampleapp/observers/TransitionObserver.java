package com.alvarium.exampleapp.observers;

import com.alvarium.Sdk;
import com.alvarium.annotators.AnnotatorException;
import com.alvarium.exampleapp.SampleData;
import com.alvarium.streams.StreamException;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.Exceptions;

/**
 * This class is responsible for data transition and calling the sdk.transit() method.
 * 
 * It takes an (Alvarium) SDK and is not expected to populate any other subjects as it is the end
 * of the annotation pipeline.
 */

public class TransitionObserver implements Observer <SampleData> {
  private final Sdk sdk;

  public TransitionObserver (Sdk sdk){
    this.sdk = sdk;
  }

  public void onNext(SampleData data) {
    try {
      sdk.transit(data.toJson().getBytes());
    } catch(AnnotatorException e) {
      // Exceptions are propagated because onNext's signature does not throw any exception
        Exceptions.propagate(
        new ObserverException("Could not annotate data during transit", e)
      );
    } catch(StreamException e) {
        Exceptions.propagate(
        new ObserverException("Could not publish data during transit", e)
      );
    }
  }
  
  @Override
  public void onError(Throwable d) {
    final Exception e = new Exception(d);
    Exceptions.propagate(
      new ObserverException("Could not publish data during transit", e)
    );
  }

  @Override
  public void onComplete() {}
  
  @Override
  public void onSubscribe(Disposable d) {}  

}

