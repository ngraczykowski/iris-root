package com.silenteight.adjudication.engine.app.scheduling;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SchedulerConfiguration {

  @Bean
  public LockProvider lockProvider(
      JdbcTemplate jdbcTemplate, PlatformTransactionManager txManager) {

    return new JdbcTemplateLockProvider(jdbcTemplate, txManager, "ae_shedlock");
  }
}
