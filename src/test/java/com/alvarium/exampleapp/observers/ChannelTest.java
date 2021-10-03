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
