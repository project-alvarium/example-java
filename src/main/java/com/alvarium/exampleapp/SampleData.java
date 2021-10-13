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
package com.alvarium.exampleapp;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import com.alvarium.sign.KeyInfo;
import com.alvarium.sign.SignException;
import com.alvarium.sign.SignProvider;
import com.alvarium.sign.SignProviderFactory;
import com.alvarium.utils.Encoder;
import com.google.gson.Gson;

/**
 * A model that is used to present example data being sent and received throughout the system
 */
public class SampleData implements Serializable {
  private final String description;
  private final String seed;
  private final String signature;

  public SampleData(KeyInfo keyInfo) throws SignException, IOException {
    this.description = randomString(128);
    this.seed = randomString(64);

    // sign seed
    final SignProviderFactory factory = new SignProviderFactory();
    final SignProvider signProvider = factory.getProvider(keyInfo.getType());
    final String privateKey = Files.readString(Paths.get(keyInfo.getPath()),
        StandardCharsets.US_ASCII);
    this.signature = signProvider.sign(Encoder.hexToBytes(privateKey), this.seed.getBytes());
  }
  
  private String randomString(int length) {
    final int lowerLimit = 97;
    final int upperLimit = 122;
    final Random random = new Random();
    final String generatedString = random.ints(lowerLimit, upperLimit + 1).limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
    return generatedString;
  }

  public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public static SampleData fromJson(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, SampleData.class);
  }

  // getters
  public String getDescription() {
    return this.description;
  }

  public String getSeed() {
    return this.seed;
  }

  public String getSignature() {
    return this.signature;
  }
}
