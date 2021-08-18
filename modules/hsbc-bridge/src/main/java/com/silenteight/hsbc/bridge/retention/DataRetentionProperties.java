package com.silenteight.hsbc.bridge.retention;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConfigurationProperties("silenteight.bridge.data.retention")
@Value
@ConstructorBinding
class DataRetentionProperties {

  int chunk;
  Duration rate;
  PersonalInformationExpired personalInformationExpired;
  AlertsExpired alertsExpired;

  @Value
  static class PersonalInformationExpired {

    boolean enabled;
    Duration duration;
  }

  @Value
  static class AlertsExpired {

    boolean enabled;
    Duration duration;
  }
}
