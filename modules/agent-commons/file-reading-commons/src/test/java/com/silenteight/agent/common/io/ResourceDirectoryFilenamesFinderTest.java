package com.silenteight.agent.common.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static java.util.stream.Collectors.toList;

class ResourceDirectoryFilenamesFinderTest {

  private final ResourceDirectoryFilenamesFinder underTest =
      new ResourceDirectoryFilenamesFinder("/resourcefileslister");

  private static String extractFilename(String s) {
    var splited = s.split("/");
    return splited[splited.length - 1];
  }

  @Test
  void readsTestResourcesCorrectly() throws IOException, URISyntaxException {
    var files = underTest.find();

    System.out.println(files.collect(toList()));
  }
}
