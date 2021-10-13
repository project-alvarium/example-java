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
