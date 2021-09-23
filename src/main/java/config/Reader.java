package config;
import com.alvarium.SdkInfo;

public interface Reader {
  public SdkInfo read (String path) throws ReaderException;
}