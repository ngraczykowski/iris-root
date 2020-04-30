package com.silenteight.auditing.bs;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
class AuditingConfiguration {

  private final Optional<PlatformTransactionManager> transactionManager;

  @Bean
  AuditingLogger auditingLogger(DataSource dataSource) {
    NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    AuditingLogger auditingLogger = new AuditingLogger(jdbcTemplate);

    if (transactionManager.isPresent()) {
      TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager.get());
      auditingLogger.setTransactionTemplate(transactionTemplate);
    }

    return auditingLogger;
  }

  @Bean
  AuditingFinder auditingFinder(DataSource dataSource) {
    return new AuditingFinder(new NamedParameterJdbcTemplate(dataSource));
  }
}
