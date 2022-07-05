package com.silenteight.agent.monitoring;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
class MonitoringConfiguration {

  @Bean
  Monitoring monitoring() {
    return new SentryMonitoring();
  }
}
