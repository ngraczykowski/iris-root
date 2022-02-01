package com.silenteight.agent.monitoring;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class MonitoringConfiguration {

  @Bean
  @ConditionalOnProperty("sentry.enabled")
  Monitoring monitoring() {
    return new SentryMonitoring();
  }
}
