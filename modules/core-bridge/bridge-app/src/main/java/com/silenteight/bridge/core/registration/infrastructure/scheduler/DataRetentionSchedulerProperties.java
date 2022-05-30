package com.silenteight.bridge.core.registration.infrastructure.scheduler;

import lombok.Builder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.bridge.data-retention")
public record DataRetentionSchedulerProperties(
    int chunk,
    DryRunMode dryRunMode,
    Duration duration
) {

  @Builder
  public DataRetentionSchedulerProperties {}

  public record DryRunMode(boolean enabled) {}

}
