package com.silenteight.adjudication.engine.analysis.matchrecommendation.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(JdbcMatchRecommendationProperties.class)
class JdbcMatchRecommendationConfiguration {

  private final DataSource dataSource;
  private final JdbcMatchRecommendationProperties properties;

  @Bean
  SelectPendingMatchesQuery selectPendingMatchesQuery() {
    return new SelectPendingMatchesQuery(properties.getPendingMatches().getFetchSize(), dataSource);
  }

}
