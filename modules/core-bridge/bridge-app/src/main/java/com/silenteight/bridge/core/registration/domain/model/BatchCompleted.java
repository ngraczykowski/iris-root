package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

public record BatchCompleted(String id,
                             String analysisName,
                             String batchMetadata) {

  @Builder
  public BatchCompleted {}
}
