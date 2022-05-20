package com.silenteight.adjudication.engine.comments.comment;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {

  private static final String SRC_TEST_RESOURCES =
      "src/test/resources/com/silenteight/adjudication/engine/comments/comment";

  public TestUtils() {
  }

  @SneakyThrows
  public static String readFile(String fileName) {
    return Files.readString(Path.of(SRC_TEST_RESOURCES, fileName));
  }
}
