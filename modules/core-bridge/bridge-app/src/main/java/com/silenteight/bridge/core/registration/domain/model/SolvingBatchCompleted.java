package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record SolvingBatchCompleted(String id,
                                    String analysisName,
                                    String batchMetadata
) {

  @Builder
  public SolvingBatchCompleted {}
}
