package com.silenteight.fab.dataprep.domain;

import lombok.NonNull;
import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;
import javax.validation.Valid;

@ConstructorBinding
@ConfigurationProperties(prefix = "feeding.learning")
@Value
@Valid
class LearningProperties {

  @NonNull
  Duration dataRetentionDuration;
}
