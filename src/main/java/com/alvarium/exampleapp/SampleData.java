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
