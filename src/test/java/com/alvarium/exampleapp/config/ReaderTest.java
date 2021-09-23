package com.alvarium.exampleapp.config;

import static org.junit.Assert.assertNotNull;

import com.alvarium.SdkInfo;

import org.junit.Test;

import config.Reader;
import config.ReaderException;
import config.ReaderFactory;
import config.ReaderType;

public class ReaderTest {
  @Test
  public void jsonReaderShouldReturnSdkInfo() throws ReaderException{
    ReaderFactory factory = new ReaderFactory();
    Reader reader = factory.getReader(ReaderType.JSON);
    String path = "./src/main/resources/config.json";
    SdkInfo sdkInfo = reader.read(path);
    assertNotNull(sdkInfo.getHash());
    assertNotNull(sdkInfo.getAnnotators());
    assertNotNull(sdkInfo.getSignature());
    assertNotNull(sdkInfo.getStream());

  }

  @Test (expected = ReaderException.class)
  public void jsonReaderShouldThrowReaderException() throws ReaderException{
    ReaderFactory factory = new ReaderFactory();
    Reader reader = factory.getReader(ReaderType.JSON);
    String path = "./src/test/java/com/alvarium/exampleapp/config/bad-config.toml";
    reader.read(path);
  }
}
