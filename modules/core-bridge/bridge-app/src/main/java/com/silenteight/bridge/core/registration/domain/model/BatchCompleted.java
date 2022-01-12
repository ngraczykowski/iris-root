package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record BatchCompleted(String id,
                             String analysisId,
                             List<String> alertIds,
                             String batchMetadata) {

  @Builder
  public BatchCompleted {}
}
