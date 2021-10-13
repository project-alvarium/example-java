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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ChannelTest {
  class MockObserver implements Observer<Integer> {
    private Boolean triggered = false;
    public void onNext(Integer value) {
      triggered = true;
    }
    public void onError(Throwable t) {}
    public void onComplete() {}
    public void onSubscribe(Disposable d) {}
    public Boolean wasTriggered() {
      return triggered;
    }
  }

  @Test
  public void channelPublishShouldTriggerObserver() {
    final Channel<Integer> channel = new Channel<Integer>();
    MockObserver observer = new MockObserver();
    channel.registerObserver(observer);
    channel.publish(1);
    channel.close();
    assertTrue(observer.wasTriggered());
  }
  
}
