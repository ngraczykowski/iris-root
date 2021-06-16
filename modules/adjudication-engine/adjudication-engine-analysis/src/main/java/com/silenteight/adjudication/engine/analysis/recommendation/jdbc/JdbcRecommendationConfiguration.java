package com.silenteight.adjudication.engine.analysis.recommendation.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(JdbcRecommendationProperties.class)
class JdbcRecommendationConfiguration {

  @Valid
  private final JdbcRecommendationProperties properties;

  @Bean
  SelectPendingAlertsQuery selectPendingAlertsQuery(DataSource dataSource) {
    return new SelectPendingAlertsQuery(dataSource, properties.getPendingAlerts().getFetchSize());
  }
}
