package com.silenteight.simulator.processing.alert.index.feed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RequestIdGeneratorTest {

  private RequestIdGenerator underTest;

  @BeforeEach
  void setUp() {
    underTest = new RequestIdGenerator();
  }

  @Test
  void shouldGenerateRequestId() {
    // when
    String requestId = underTest.generate();

    // then
    assertThat(requestId).isNotBlank();
  }
}
