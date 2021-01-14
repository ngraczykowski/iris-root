package com.silenteight.agent.configloader;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigsPathFinderTest {

  @Test
  void shouldThrowExceptionIfApplicationConfigCannotBeFound() {
    // given
    ConfigsPathFinder configsPathFinder = new ConfigsPathFinder("wrong-name");

    // expect
    assertThrows(IllegalStateException.class, configsPathFinder::find);
  }
}
