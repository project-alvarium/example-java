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