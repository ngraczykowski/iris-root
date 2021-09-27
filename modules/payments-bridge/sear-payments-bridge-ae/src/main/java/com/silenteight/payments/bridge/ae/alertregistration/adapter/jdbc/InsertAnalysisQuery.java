package com.silenteight.payments.bridge.ae.alertregistration.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;

@Component
@RequiredArgsConstructor
class InsertAnalysisQuery {

  @Language("PostgreSQL")
  private static final String SQL = "INSERT INTO pb_analysis VALUES (?, now())";

  private final JdbcTemplate jdbcTemplate;
  private final PlatformTransactionManager platformTransactionManager;

  Optional<Long> update(long analysisId) {
    DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();

    TransactionStatus status =
        platformTransactionManager.getTransaction(paramTransactionDefinition);
    jdbcTemplate.update(SQL, analysisId);
    platformTransactionManager.commit(status);
    return Optional.of(analysisId);
  }
}
