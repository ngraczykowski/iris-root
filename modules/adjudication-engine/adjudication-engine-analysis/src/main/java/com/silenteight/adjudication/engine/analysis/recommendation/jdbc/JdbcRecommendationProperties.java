package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ConfigurationProperties(prefix = "ae.recommendation.jdbc")
@Data
@Validated
class JdbcRecommendationProperties {

  static final int DEFAULT_FETCH_SIZE = 8_192;

  @Valid
  @NestedConfigurationProperty
  private PendingAlerts pendingAlerts = new PendingAlerts();

  @Data
  @Validated
  static class PendingAlerts {

    @Min(1)
    @Max(1_048_576)
    private int fetchSize = DEFAULT_FETCH_SIZE;
  }
}
