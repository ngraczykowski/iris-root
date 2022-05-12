package com.silenteight.warehouse.production.persistence.insert;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class PersistenceConfiguration {

  @Bean
  PersistenceService persistenceService(
      NamedParameterJdbcTemplate jdbcTemplate,
      AlertPersistenceService alertPersistenceService,
      LabelPersistenceService labelPersistenceService,
      MatchPersistenceService matchPersistenceService) {

    return new PersistenceService(
        jdbcTemplate,
        alertPersistenceService,
        labelPersistenceService,
        matchPersistenceService);
  }

  @Bean
  AlertPersistenceService alertPersistenceService() {
    return new AlertPersistenceService();
  }

  @Bean
  LabelPersistenceService labelPersistenceService() {
    return new LabelPersistenceService();
  }

  @Bean
  MatchPersistenceService matchPersistenceService() {
    return new MatchPersistenceService();
  }
}
