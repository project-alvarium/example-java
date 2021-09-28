package com.alvarium.exampleapp;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.alvarium.sign.KeyInfo;
import com.alvarium.sign.SignException;
import com.alvarium.sign.SignProvider;
import com.alvarium.sign.SignProviderFactory;
import com.alvarium.sign.SignType;
import com.alvarium.utils.Encoder;

import org.junit.Test;

public class SampleDataTest {
  @Test
  public void constructorShouldRandomizeAndSignSeed() throws SignException, IOException {
    final String privateKeyPath = "./src/test/java/com/alvarium/exampleapp/private.key";
    final KeyInfo keyInfo = new KeyInfo(privateKeyPath, SignType.Ed25519);
    final SampleData sampleData = new SampleData(keyInfo);
    System.out.println(sampleData.toJson());

    assertNotNull(sampleData.getDescription());
    assertNotNull(sampleData.getSeed());
    assertNotNull(sampleData.getSignature());
  }

  @Test
  public void signatureVerificationShouldNotThrow() throws IOException, SignException {
    final String privateKeyPath = "./src/test/java/com/alvarium/exampleapp/private.key";
    final String publicKeyPath = "./src/test/java/com/alvarium/exampleapp/public.key";
    final KeyInfo keyInfo = new KeyInfo(privateKeyPath, SignType.Ed25519);
    final SampleData sampleData = new SampleData(keyInfo);

    final String publicKey = Files.readString(Paths.get(publicKeyPath), StandardCharsets.US_ASCII);
    final byte[] publicKeyDecoded = Encoder.hexToBytes(publicKey);
    final SignProviderFactory factory = new SignProviderFactory();
    final SignProvider signProvider = factory.getProvider(keyInfo.getType());
    signProvider.verify(publicKeyDecoded, sampleData.getSeed().getBytes(), 
        Encoder.hexToBytes(sampleData.getSignature()));
  }
}
