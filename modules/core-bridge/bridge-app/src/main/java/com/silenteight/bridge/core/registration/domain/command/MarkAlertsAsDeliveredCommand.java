package com.silenteight.bridge.core.registration.domain.command;

import lombok.Builder;

import java.util.List;

public record MarkAlertsAsDeliveredCommand(
    String batchId,
    String analysisName,
    List<String> alertNames
) {

  @Builder
  public MarkAlertsAsDeliveredCommand {}
}
