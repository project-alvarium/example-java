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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.alvarium.Sdk;
import com.alvarium.SdkInfo;
import com.alvarium.exampleapp.SampleData;
import com.alvarium.sign.KeyInfo;

import org.junit.Test;
import org.mockito.Mockito;

import config.Reader;
import config.ReaderFactory;
import config.ReaderType;
import io.reactivex.rxjava3.core.Observer;

public class MutationObserverTest {
  @Test
  public void mutationObserverShouldMutateAndPublish() throws Exception{
    
    Channel<SampleData> srcChannel = new Channel<SampleData>();
    Channel<SampleData> destinationChannel =new Channel<SampleData>();
    final Sdk sdk = Mockito.mock(Sdk.class);
    ReaderFactory readerFactory = new ReaderFactory();
    Reader reader = readerFactory.getReader(ReaderType.JSON);

    SdkInfo sdkInfo = reader.read("./src/main/resources/config.json");
    final KeyInfo keyInfo = sdkInfo.getSignature().getPrivateKey();

    final SampleData oldData = new SampleData(keyInfo);

    final Observer<SampleData> mutationObserver = new MutationObserver(sdk,keyInfo,destinationChannel);
    srcChannel.registerObserver(mutationObserver);
    srcChannel.publish(oldData);
    
    //Using any() because mutate generates its own sample data as the new data
    //and Mockito checks argument equality 
    verify(sdk).mutate(any(byte[].class),any(byte[].class));

    

  }
  
}
