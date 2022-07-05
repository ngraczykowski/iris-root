/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.simulator.processing.alert.index.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.silenteight.simulator.processing.alert.index.domain.State.ACKED;
import static com.silenteight.simulator.processing.alert.index.domain.State.UNKNOWN;
import static org.assertj.core.api.Assertions.*;

class StateTest {

  @DisplayName("Passing string value should be mapped to State")
  @Test
  void passingString_shouldMapToState() {
    assertThat(State.from(ACKED.toString())).isEqualTo(ACKED);
    assertThat(State.from("ACKED")).isEqualTo(ACKED);
    assertThat(State.from("acked")).isEqualTo(UNKNOWN);
    assertThat(State.from("ACKED1")).isEqualTo(UNKNOWN);
  }
}
