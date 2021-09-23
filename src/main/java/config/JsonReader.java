package config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.alvarium.SdkInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;


class JsonReader implements Reader{
  public SdkInfo read(String path) throws ReaderException{
    try{
      String content=Files.readString(Paths.get(path));
      JsonObject json = JsonParser.parseString(content).getAsJsonObject();
      return SdkInfo.fromJson(json.get("sdk").toString());
    }catch(IOException e){
      throw new ReaderException("Failed to read configuration", e);
    }catch(JsonParseException e){
      throw new ReaderException("Failed to parse configuration", e);
    }
  }
}