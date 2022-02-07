package com.silenteight.warehouse.indexer.query.single;

import com.silenteight.warehouse.indexer.alert.AlertPostgresRepository;
import com.silenteight.warehouse.indexer.alert.AlertRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SingleAlertQueryConfiguration {

  @Bean
  AlertRepository createAlertRepository(JdbcTemplate jdbcTemplate) {
    return new AlertPostgresRepository(jdbcTemplate);
  }

  @Bean
  RandomAlertService randomPostgresAlertQueryService(AlertRepository alertRepository) {
    return new RandomPostgresSearchAlertQueryService(alertRepository);
  }
}
