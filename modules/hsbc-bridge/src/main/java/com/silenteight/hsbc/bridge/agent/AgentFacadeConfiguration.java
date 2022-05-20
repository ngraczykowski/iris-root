package com.silenteight.hsbc.bridge.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class AgentFacadeConfiguration {

  private final IsPepMessageSender isPepMessageSender;
  private final HistoricalDecisionMessageSender historicalMessageSender;
  private final CustomDateTimeFormatter dateTimeFormatter;

  @Bean
  AgentFacade agentFacade() {
    return new AgentFacade(agentIsPep(), agentHistorical());
  }

  private AgentIsPep agentIsPep() {
    return new AgentIsPep(isPepMessageSender, isPepRequestCreator());
  }

  private AgentHistorical agentHistorical() {
    return new AgentHistorical(historicalMessageSender, historicalDecisionRequestCreator());
  }

  private IsPepRequestCreator isPepRequestCreator() {
    return new IsPepRequestCreator(timestampMapper());
  }

  private HistoricalDecisionRequestCreator historicalDecisionRequestCreator() {
    return new HistoricalDecisionRequestCreator(timestampMapper());
  }

  private AgentTimestampMapper timestampMapper() {
    return new AgentTimestampMapper(dateTimeFormatter.getDateTimeFormatter());
  }
}
