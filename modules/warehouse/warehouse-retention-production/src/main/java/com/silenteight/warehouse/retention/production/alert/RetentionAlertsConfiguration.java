package com.silenteight.warehouse.retention.production.alert;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(EraseAlertsProperties.class)
class RetentionAlertsConfiguration {

  @Bean
  EraseAlertsUseCase eraseAlertsUseCase(
      @Valid EraseAlertsProperties properties,
      NamedParameterJdbcTemplate jdbcTemplate) {

    return new EraseAlertsUseCase(properties.getBatchSize(), jdbcTemplate);
  }
}
