package com.silenteight.bridge.core.registration.domain.model;

import java.util.List;

public record AlertsAddedToAnalysis(Status status, List<String> alertNames) {

  public enum Status {
    SUCCESS, FAILURE
  }
}
