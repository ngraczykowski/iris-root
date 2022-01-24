package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record BatchWithAlerts(
    String batchId,
    String policyId,
    List<AlertWithMatches> alerts
) {

  @Builder
  public BatchWithAlerts {}
}
