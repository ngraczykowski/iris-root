package com.silenteight.connector.ftcc.request.domain;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import javax.validation.constraints.NotNull;

@Value
@Validated
@ConfigurationProperties(prefix = "ftcc.retention.request")
@ConstructorBinding
class RequestRetentionProperties {

  @NotNull
  Duration dataRetentionDuration;
}
