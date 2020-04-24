package com.silenteight.auditing.bs;

import lombok.Setter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
class AuditingConfiguration {

  @Setter
  private PlatformTransactionManager transactionManager;

  @Bean
  AuditingLogger auditingLogger(DataSource dataSource) {
    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    AuditingLogger auditingLogger = new AuditingLogger(jdbcTemplate);

    if (transactionManager != null) {
      TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
      auditingLogger.setTransactionTemplate(transactionTemplate);
    }

    return auditingLogger;
  }
}
