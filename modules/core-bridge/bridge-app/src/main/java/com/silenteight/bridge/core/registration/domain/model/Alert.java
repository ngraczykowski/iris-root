package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record Alert(String name,
                    Status status,
                    String alertId,
                    String batchId,
                    List<Match> matches,
                    String errorDescription) {

  @Builder
  public Alert {
  }

  public enum Status {
    REGISTERED, PROCESSING, RECOMMENDED, ERROR
  }
}
