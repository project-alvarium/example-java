package config;

public class ReaderFactory {
  public Reader getReader(ReaderType type) throws ReaderException{
    switch(type){
      case JSON: return new JsonReader();
      default: throw new ReaderException("Concrete type doesn't exist", null);
    }
  }
}
