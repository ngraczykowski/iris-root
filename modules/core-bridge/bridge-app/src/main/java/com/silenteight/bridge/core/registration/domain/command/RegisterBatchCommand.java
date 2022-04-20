package com.silenteight.bridge.core.registration.domain.command;

import lombok.Builder;

public record RegisterBatchCommand(
    String id,
    Long alertCount,
    String batchMetadata,
    Integer batchPriority,
    boolean isSimulation
) {

  @Builder
  public RegisterBatchCommand {}

}
