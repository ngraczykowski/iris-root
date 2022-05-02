package com.silenteight.connector.ftcc.request.domain;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties(prefix = "ftcc.retention.request")
@ConstructorBinding
class RequestRetentionProperties {

  @NotNull
  private final Duration dataRetentionDuration;
}
