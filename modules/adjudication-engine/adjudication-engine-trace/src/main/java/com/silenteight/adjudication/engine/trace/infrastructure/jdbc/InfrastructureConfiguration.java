package com.silenteight.adjudication.engine.trace.infrastructure.jdbc;

import com.silenteight.adjudication.engine.trace.domain.TraceRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class InfrastructureConfiguration {

  @Bean
  TraceRepository traceRepository(JdbcTemplate jdbcTemplate) {
    return new TraceJdbcRepository(jdbcTemplate);
  }
}
