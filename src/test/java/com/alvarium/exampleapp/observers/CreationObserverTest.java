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

import com.alvarium.Sdk;
import com.alvarium.exampleapp.SampleData;
import com.alvarium.sign.KeyInfo;
import com.alvarium.sign.SignType;
import com.alvarium.utils.PropertyBag;

import org.junit.Test;

import io.reactivex.rxjava3.core.Observer;

public class CreationObserverTest {

  @Test
  public void creationObserverShouldCreateAndPublish() throws Exception {
    final String privateKeyPath = "./src/test/java/com/alvarium/exampleapp/private.key";
    final KeyInfo keyInfo = new KeyInfo(privateKeyPath, SignType.Ed25519);
    final SampleData sampleData = new SampleData(keyInfo);

    final Channel<SampleData> source = new Channel<SampleData>();
    final Channel<SampleData> destination = new Channel<SampleData>();

    final Sdk sdk = new Sdk() {
      @Override
      public void create(byte[] data) {
        System.out.println("Creating annotation for: ");
        System.out.println(new String(data));
      }

      @Override
      public void create(PropertyBag ctx, byte[] data) {
        System.out.println(new String(data));
      }

      @Override
      public void mutate(byte[] oldData, byte[] newData) {
        System.out.println(new String(newData));
      }

      @Override
      public void mutate(PropertyBag ctx, byte[] oldData, byte[] newData) {
        System.out.println(new String(newData));
      }

      @Override
      public void transit(byte[] data) {
        System.out.println(new String(data));
      }

      @Override
      public void transit(PropertyBag ctx, byte[] data) {
        System.out.println(new String(data));
      }

      @Override
      public void close() {

      }
    };
    final Observer<SampleData> creationObserver = new CreationObserver(sdk, destination);

    source.registerObserver(creationObserver);

    source.publish(sampleData);
  }

}
