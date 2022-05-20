package com.silenteight.payments.bridge.svb.learning.job;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties(prefix = "pb.grpc.client.datasource")
@Validated
@Data
class DatasourceClientProperties {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

  private Duration timeout = DEFAULT_TIMEOUT;
}
