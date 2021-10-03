package com.alvarium.exampleapp.observers;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;

/**
 * Creates a channel of communication using the observer pattern.
 * 
 * The main objective of this class is to be a wrapper around subjects for better member naming and
 * also to protect additional subject methods.
 * 
 * This class takes a generic type which is used to infer the type of data this channel will hold.
 */
public class Channel<T>{
  private PublishSubject<T> subject;

  public Channel() {
    subject = PublishSubject.create();
  }

  /**
   * Subscribes an observer to this channel, meaning that this observer will now trigger its onNext
   * whenever a new message is emmited on this channel
   * @param observer
   */
  public void registerObserver(Observer<T> observer) {
    subject.subscribe(observer);
  }

  /**
   * Add a new message to this channel
   * @param data
   */
  public void publish(T data) {
    subject.onNext(data);
  }

  /**
   * Close and remove any allocated resources by this channel
   */
  public void close() {
    subject.onComplete();
  }
}