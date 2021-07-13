package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AgentConfiguration {

  private final IsPepMessageSender isPepMessageSender;
  private final HistoricalDecisionMessageSender historicalMessageSender;

  @Bean
  Agent agent() {
    return new Agent(isPepMessageSender, historicalMessageSender);
  }
}
