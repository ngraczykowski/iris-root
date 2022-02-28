package com.silenteight.payments.bridge.data.retention.service;

import lombok.Data;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.data-retention")
class DataRetentionProperties {

  @NestedConfigurationProperty
  private AlertData alertData = new AlertData();

  @NestedConfigurationProperty
  private PersonalInformation personalInformation = new PersonalInformation();

  @NestedConfigurationProperty
  private File file = new File();

  @Data
  static class AlertData {
    @NotNull
    Duration expiration = Duration.ofHours(48);
  }

  @Data
  static class PersonalInformation {
    @NotNull
    Duration expiration = Duration.ofDays(151);
  }

  @Data
  static class File {
    @NotNull
    Duration expiration = Duration.ofDays(151);
  }
}
