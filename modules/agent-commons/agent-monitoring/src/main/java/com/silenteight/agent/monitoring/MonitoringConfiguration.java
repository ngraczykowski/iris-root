package com.silenteight.agent.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Slf4j
class MonitoringConfiguration {

  @Bean
  Monitoring monitoring() {
    return new SentryMonitoring();
  }
}
