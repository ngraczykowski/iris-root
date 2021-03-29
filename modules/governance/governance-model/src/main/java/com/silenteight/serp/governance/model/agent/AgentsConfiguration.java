package com.silenteight.serp.governance.model.agent;

import com.silenteight.serp.governance.model.ModelProperties;
import com.silenteight.serp.governance.model.agent.config.AgentDiscovery;
import com.silenteight.serp.governance.model.agent.details.AgentDetailsClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@EnableConfigurationProperties(ModelProperties.class)
class AgentsConfiguration {

  @Bean
  AgentDiscovery agentDiscovery(
      ObjectMapper objectMapper,
      ModelProperties modelProperties,
      ResourceLoader resourceLoader) {

    return new AgentDiscovery(
        modelProperties.getAgentConfigurationSource(), objectMapper, resourceLoader);
  }

  @Bean
  AgentDetailsClient agentDetailsClient(
      ObjectMapper objectMapper,
      ModelProperties modelProperties,
      ResourceLoader resourceLoader) {

    return new AgentDetailsClient(
        modelProperties.getAgentDetailsSource(), objectMapper, resourceLoader);
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
