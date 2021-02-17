package com.silenteight.agent.common.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class ResourceDirectoryFilenamesFinderTest {

  private final ResourceDirectoryFilenamesFinder underTest =
      new ResourceDirectoryFilenamesFinder("/resourcefileslister");

  @Test
  void readsTestResourcesCorrectly() throws IOException {
    var files = underTest.find();

    assertThat(files.count()).isEqualTo(2);
  }
}
