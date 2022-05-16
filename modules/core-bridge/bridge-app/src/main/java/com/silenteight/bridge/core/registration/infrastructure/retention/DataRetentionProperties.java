package com.silenteight.bridge.core.registration.infrastructure.retention;

import lombok.Builder;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConstructorBinding
@ConfigurationProperties(prefix = "silenteight.bridge.data-retention")
public record DataRetentionProperties(
    int chunk,
    DryRunMode dryRunMode,
    PersonalInformationExpired personalInformationExpired,
    AlertsExpired alertsExpired) {

  @Builder
  public DataRetentionProperties {}

  public record DryRunMode(boolean enabled) {}

  public record PersonalInformationExpired(boolean enabled, Duration duration) {}

  public record AlertsExpired(boolean enabled, Duration duration) {}
}
