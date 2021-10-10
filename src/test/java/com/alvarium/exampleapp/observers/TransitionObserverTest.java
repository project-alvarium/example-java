package com.alvarium.exampleapp.observers;

import static org.mockito.Mockito.verify;

import com.alvarium.Sdk;
import com.alvarium.exampleapp.SampleData;
import com.alvarium.sign.KeyInfo;
import com.alvarium.sign.SignType;

import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.rxjava3.core.Observer;


public class TransitionObserverTest {
 
  @Test
  public void transitionObserverShouldTransitAndPublish()throws Exception{
    final String privateKeyPath = "./src/test/java/com/alvarium/exampleapp/private.key";
    final KeyInfo keyInfo = new KeyInfo(privateKeyPath, SignType.Ed25519);
    final SampleData sampleData = new SampleData(keyInfo);
    final Sdk sdk=Mockito.mock(Sdk.class);
  
    Channel<SampleData> source = new Channel<SampleData>();
    Observer<SampleData> transitionObserver = new TransitionObserver(sdk); 

    source.registerObserver(transitionObserver);
    source.publish(sampleData);

    verify(sdk).transit(sampleData.toJson().getBytes());
  }
  
}
