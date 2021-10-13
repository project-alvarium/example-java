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