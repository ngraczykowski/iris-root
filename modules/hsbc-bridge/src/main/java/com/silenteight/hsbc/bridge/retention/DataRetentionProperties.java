package com.silenteight.hsbc.bridge.retention;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@ConfigurationProperties("silenteight.bridge.data.retention")
@Value
@ConstructorBinding
class DataRetentionProperties {

  boolean enabled;
  Duration rate;
  Duration duration;
}
