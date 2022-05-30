package com.silenteight.bridge.core.registration.infrastructure.scheduler;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties({
    DataRetentionSchedulerProperties.class
})
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "${silenteight.bridge.data-retention.lock-at-most-for}")
class DataRetentionSchedulerConfiguration {

  private static final String SHEDLOCK_TABLE_NAME = "core_bridge_shedlock";

  @Bean
  LockProvider lockProvider(JdbcTemplate jdbcTemplate) {
    return new JdbcTemplateLockProvider(jdbcTemplate, SHEDLOCK_TABLE_NAME);
  }
}
