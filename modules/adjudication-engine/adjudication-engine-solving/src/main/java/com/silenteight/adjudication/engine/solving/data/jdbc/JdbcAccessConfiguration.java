package com.silenteight.adjudication.engine.solving.data.jdbc;

import com.silenteight.adjudication.engine.solving.data.MatchFeaturesFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JdbcAccessConfiguration {

  @Bean
  MatchFeaturesFacade matchFeaturesFacade(
      SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery) {
    return new JdbcAnalysisFeaturesFacade(selectAnalysisFeaturesQuery);
  }
}
