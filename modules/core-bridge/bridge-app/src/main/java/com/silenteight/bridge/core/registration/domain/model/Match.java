package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record Match(String name,
                    Status status,
                    String matchId) {

  @Builder
  public Match {
  }

  public enum Status {
    REGISTERED, PROCESSING, ERROR
  }
}
