package com.silenteight.agent.common.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.*;

class SimpleRowsReaderTest {

  @Test
  void givenThreeLines_readsCorrectly() {
    var input = "some\ntext\nto\nread";

    var inputStream = new ByteArrayInputStream(input.getBytes(UTF_8));
    var underTest = new SimpleRowsReader(() -> inputStream);

    var actual = underTest.stream();

    assertThat(actual).containsExactly("some", "text", "to", "read");
  }
}
