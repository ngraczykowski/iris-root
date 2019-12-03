package com.silenteight.sens.webapp.common.database;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.transaction.PlatformTransactionManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;

@RequiredArgsConstructor
@ConditionalOnClass(JpaTransactionManager.class)
public class TransactionManagerAutoConfiguration {

  @Bean
  PlatformTransactionManagerCustomizer<JpaTransactionManager> jpaTransactionManagerCustomizer() {
    return transactionManager -> transactionManager.setNestedTransactionAllowed(true);
  }
}
