package com.silenteight.auditing.bs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
class AuditingConfiguration {

  @Bean
  AuditingLogger auditingLogger(DataSource dataSource) {
    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    return new AuditingLogger(jdbcTemplate);
  }
}
