package com.silenteight.agent.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Slf4j
class MonitoringConfiguration {

  @Bean
  @ConditionalOnProperty(
      value = "sentry.enabled",
      havingValue = "true")
  Monitoring monitoring() {
    return new SentryMonitoring();
  }

  @Bean
  @ConditionalOnProperty(
      value = "sentry.enabled",
      havingValue = "false")
  Monitoring dummyMonitoring() {
    return t -> log.info("Dummy monitoring for local environment");
  }
}
