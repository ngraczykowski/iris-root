package com.silenteight.sens.webapp.common.support.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.io.File.separator;
import static java.nio.file.Files.createDirectories;

public class FileLineWriter {

  public void write(String dirPath, String fileName, Stream<String> lines) throws IOException {
    createNotExistingDirectories(dirPath);
    File file = toFile(dirPath, fileName);
    try (PrintWriter writer = new PrintWriter(file)) {
      lines.forEach(writer::println);
    }
  }

  private static void createNotExistingDirectories(String dirPath) throws IOException {
    createDirectories(Paths.get(dirPath));
  }

  private static File toFile(String dirPath, String fileName) {
    return new File(dirPath + separator + fileName);
  }
}
