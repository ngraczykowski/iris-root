package com.silenteight.sens.webapp.common.support.csv;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

public class FileLineWriter {

  public void write(String filePath, Stream<String> lines) throws IOException {
    File file = new File(filePath);
    try (PrintWriter writer = new PrintWriter(file)) {
      lines.forEach(writer::println);
    }
  }
}
