package com.silenteight.agent.configloader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigsPathFinderTest {

  @Test
  void shouldThrowExceptionIfApplicationConfigCannotBeFound() {
    // expect
    assertThrows(
        IllegalStateException.class,
        () -> ConfigsPathFinder.findDirectory("wrong-name"));
  }
}
