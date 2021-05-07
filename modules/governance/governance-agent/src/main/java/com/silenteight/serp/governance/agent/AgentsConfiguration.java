package com.silenteight.serp.governance.agent;

import com.silenteight.serp.governance.agent.config.AgentDiscovery;
import com.silenteight.serp.governance.agent.details.AgentDetailsClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(AgentProperties.class)
class AgentsConfiguration {

  @Bean
  AgentDiscovery agentDiscovery(
      ObjectMapper objectMapper,
      @Valid  AgentProperties agentProperties,
      ResourceLoader resourceLoader) {

    return new AgentDiscovery(
        agentProperties.getAgentConfigurationSource(), objectMapper, resourceLoader);
  }

  @Bean
  AgentDetailsClient agentDetailsClient(
      ObjectMapper objectMapper,
      @Valid AgentProperties agentProperties,
      ResourceLoader resourceLoader) {

    return new AgentDetailsClient(
        agentProperties.getAgentDetailsSource(), objectMapper, resourceLoader);
  }

  @Bean
  AgentsRegistry agentRegistry(
      AgentDiscovery agentDiscovery,
      AgentDetailsClient agentDetailsClient) {

    return new AgentsRegistry(agentDiscovery, agentDetailsClient);
  }

  @Bean
  AgentMappingService agentMappingService(AgentsRegistry agentsRegistry) {
    return new AgentMappingService(agentsRegistry);
  }
}
