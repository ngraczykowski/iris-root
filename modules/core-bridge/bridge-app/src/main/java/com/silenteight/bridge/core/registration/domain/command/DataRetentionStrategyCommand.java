package com.silenteight.bridge.core.registration.domain.command;

import lombok.Builder;

import com.silenteight.bridge.core.registration.domain.model.AlertToRetention;
import com.silenteight.bridge.core.registration.domain.model.DataRetentionMode;

import java.time.Instant;
import java.util.List;

public record DataRetentionStrategyCommand(
    DataRetentionMode mode,
    Instant expirationDate,
    List<AlertToRetention> alerts,
    int chunkSize) {

  @Builder
  public DataRetentionStrategyCommand {}
}
