package com.silenteight.simulator.processing.alert.index.domain;

import lombok.NonNull;

import java.util.Arrays;

public enum State {
  /**
   * Message is in sent state to WH
   */
  SENT,
  /**
   * Message is indexed by WH and acked in SIMULATOR
   */
  ACKED,
  /**
   * DB model accepts random state value, different from SENT/ACKED. In that case mapped to UNKNOWN.
   */
  UNKNOWN;

  /**
   * Factory
   * @param state string
   * @return {@code State} or {@code UNDEFINED} if not exist
   */
  public static State from(@NonNull String state) {
    return Arrays.stream(State.values())
        .filter(s -> s.name().equals(state))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
