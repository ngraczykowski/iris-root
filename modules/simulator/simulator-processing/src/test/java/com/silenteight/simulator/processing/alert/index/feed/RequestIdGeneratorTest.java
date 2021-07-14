package com.silenteight.simulator.processing.alert.index.feed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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
