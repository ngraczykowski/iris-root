package com.silenteight.warehouse.qa.processing.update;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
class QaUpdateConfiguration {

  @Bean
  QaUpdateService qaPersistenceService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    return new QaUpdateService(namedParameterJdbcTemplate);
  }
}
