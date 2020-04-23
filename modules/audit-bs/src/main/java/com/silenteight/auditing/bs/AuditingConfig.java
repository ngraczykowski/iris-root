package com.silenteight.auditing.bs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
class AuditingConfig {

  @Bean
  public AuditingLogger auditingLogger(DataSource dataSource) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    jdbcTemplate.setDataSource(dataSource);
    return new AuditingLogger(jdbcTemplate);
  }
}
