package com.silenteight.bridge.core.registration.domain.command;

import lombok.Builder;

import com.silenteight.bridge.core.registration.domain.model.DataRetentionType;

import java.time.Duration;

public record StartDataRetentionCommand(
    DataRetentionType type,
    Duration duration,
    int chunkSize) {

  @Builder
  public StartDataRetentionCommand {}
}
