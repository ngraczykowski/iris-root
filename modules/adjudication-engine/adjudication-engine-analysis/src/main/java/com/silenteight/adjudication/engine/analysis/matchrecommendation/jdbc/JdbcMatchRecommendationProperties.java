package com.silenteight.adjudication.engine.analysis.matchrecommendation.jdbc;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ConfigurationProperties(prefix = "ae.match-recommendation.jdbc")
@Data
@Validated
class JdbcMatchRecommendationProperties {

  static final int DEFAULT_FETCH_SIZE = 1_024;

  @Valid
  @NestedConfigurationProperty
  private PendingMatches pendingMatches = new PendingMatches();

  @Data
  @Validated
  static class PendingMatches {

    @Min(1)
    @Max(1_048_576)
    private int fetchSize = DEFAULT_FETCH_SIZE;
  }

}
