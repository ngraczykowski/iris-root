package com.silenteight.fab.dataprep.domain;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(LearningProperties.class)
@EnableTransactionManagement
@EnableScheduling
class LearningConfiguration {

  @Bean
  Clock clock() {
    return Clock.systemUTC();
  }
}
