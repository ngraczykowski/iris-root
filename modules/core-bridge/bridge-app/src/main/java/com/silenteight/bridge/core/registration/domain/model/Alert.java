package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record Alert(String name,
                    AlertStatus status,
                    String alertId,
                    String batchId,
                    String metadata,
                    List<Match> matches,
                    String errorDescription) {

  @Builder
  public Alert {
  }
}
