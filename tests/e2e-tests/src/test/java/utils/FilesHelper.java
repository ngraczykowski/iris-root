package utils;

import lombok.SneakyThrows;

import java.io.InputStream;

public class FilesHelper {

  @SneakyThrows
  public int getRowsNumber(InputStream content) {
    try (content) {
      byte[] c = new byte[1024];
      int count = 0;
      int readChars;
      boolean empty = true;
      while ((readChars = content.read(c)) != -1) {
        empty = false;
        for (int i = 0; i < readChars; ++i) {
          if (c[i] == '\n') {
            ++count;
          }
        }
      }
      return (count == 0 && !empty) ? 1 : count;
    }
  }
}
