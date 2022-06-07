package com.silenteight.adjudication.engine.solving.data.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class JdbcAccessConfiguration {

  @Bean
  MatchFeaturesFacade matchFeaturesFacade(
      SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery) {
    return new MatchFeaturesFacade(selectAnalysisFeaturesQuery);
  }

  @Bean
  SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery(NamedParameterJdbcTemplate jdbcTemplate) {
    return new SelectAnalysisFeaturesQuery(jdbcTemplate);
  }
}
