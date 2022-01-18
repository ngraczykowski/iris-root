package com.silenteight.bridge.core.registration.domain.model;

import lombok.Builder;

import java.util.List;

public record BatchCompleted(String id,
                             String analysisId,
                             String batchMetadata,
                             List<String> alertIds,
                             BatchStatistics statistics) {

  @Builder
  public BatchCompleted {}
}
