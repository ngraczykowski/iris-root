package com.silenteight.adjudication.engine.solving.data.jdbc;

import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class JdbcAccessConfiguration {

  @Bean
  MatchFeatureDataAccess matchFeatureDataAccess(
      SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery) {
    return new JdbcMatchFeaturesDataAccess(selectAnalysisFeaturesQuery);
  }

  @Bean
  SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery(NamedParameterJdbcTemplate jdbcTemplate) {
    return new SelectAnalysisFeaturesQuery(jdbcTemplate);
  }
}
