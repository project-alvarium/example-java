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

import java.io.IOException;

import com.alvarium.Sdk;
import com.alvarium.annotators.AnnotatorException;
import com.alvarium.exampleapp.SampleData;
import com.alvarium.sign.KeyInfo;
import com.alvarium.sign.SignException;
import com.alvarium.streams.StreamException;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.exceptions.Exceptions;

public class MutationObserver implements Observer<SampleData>{
  private final Sdk sdk;
  private final KeyInfo keyInfo;

  private final Channel<SampleData> destinationChannel; 

  public MutationObserver(Sdk sdk, KeyInfo keyInfo,Channel<SampleData> destinationChannel) {
    this.sdk = sdk;
    this.keyInfo=keyInfo;
    this.destinationChannel = destinationChannel;
  } 

  public void onNext(SampleData oldData) {
    try {
      SampleData sampleData = new SampleData(keyInfo);
      sdk.mutate(oldData.toJson().getBytes(), sampleData.toJson().getBytes());
      destinationChannel.publish(sampleData);
    } catch(AnnotatorException e) {
      Exceptions.propagate(
          new ObserverException("Could not annotate data during mutate", e)
      );
    } catch(StreamException e) {
      Exceptions.propagate(
          new ObserverException("Could not publish data during mutate", e)
      );
    }catch(SignException e) {
      Exceptions.propagate(
          new ObserverException("Could not sign sample data", e)
      );
  }catch(IOException e) {
    Exceptions.propagate(
        new ObserverException("Could not read key", e)
    );
  }
  }

  @Override
  public void onError(Throwable d) {
    final Exception e = new Exception(d);
    Exceptions.propagate(
        new ObserverException("Could not publish data during mutate", e)
    );
  }

  @Override
  public void onComplete() {}

  @Override
  public void onSubscribe(Disposable d) {}

}