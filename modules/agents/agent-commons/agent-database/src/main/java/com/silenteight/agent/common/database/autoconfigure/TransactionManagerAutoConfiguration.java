package com.silenteight.agent.common.database.autoconfigure;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.transaction.PlatformTransactionManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@ConditionalOnClass(JpaTransactionManager.class)
public class TransactionManagerAutoConfiguration {

  @Bean
  PlatformTransactionManagerCustomizer<PlatformTransactionManager>
  jpaTransactionManagerCustomizer() {
    return transactionManager -> {
      if (transactionManager instanceof JpaTransactionManager) {
        ((JpaTransactionManager) transactionManager).setNestedTransactionAllowed(true);
      }
    };
  }
}
