package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Configuration
class InsertMatchJdbcConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  InsertMatchQuery registerMatchQuery() {
    return new InsertMatchQuery(jdbcTemplate);
  }
}
