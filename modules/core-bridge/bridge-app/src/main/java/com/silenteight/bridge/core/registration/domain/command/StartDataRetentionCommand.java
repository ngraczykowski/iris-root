package com.silenteight.bridge.core.registration.domain.command;

import lombok.Builder;

import com.silenteight.bridge.core.registration.domain.model.DataRetentionMode;

import java.time.Duration;

public record StartDataRetentionCommand(
    DataRetentionMode mode,
    Duration duration,
    int chunkSize) {

  @Builder
  public StartDataRetentionCommand {}
}
