package com.silenteight.scb.feeding.infrastructure;

import com.silenteight.scb.feeding.domain.agentinput.NameAgentInputCreator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentInputConfiguration {

  @Bean
  NameAgentInputCreator nameAgentInputCreator() {
    return new NameAgentInputCreator();
  }
}
