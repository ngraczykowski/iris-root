package com.silenteight.payments.bridge.datasource.adapter;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties(prefix = "pb.grpc.client.create-agent-inputs")
@Validated
@Data
class CreateAgentInputsClientProperties {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

  private Duration timeout = DEFAULT_TIMEOUT;
}
