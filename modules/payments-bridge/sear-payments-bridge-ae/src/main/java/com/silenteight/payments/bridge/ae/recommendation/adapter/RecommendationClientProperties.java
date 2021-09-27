package com.silenteight.payments.bridge.ae.recommendation.adapter;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties("pb.grpc.client.recommendation")
@Data
@Validated
class RecommendationClientProperties {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

  private Duration timeout = DEFAULT_TIMEOUT;
}
