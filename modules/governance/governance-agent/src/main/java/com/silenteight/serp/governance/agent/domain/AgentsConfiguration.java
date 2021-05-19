package com.silenteight.serp.governance.agent.domain;

import com.silenteight.serp.governance.agent.domain.file.config.AgentDiscovery;
import com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsClient;
import com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

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
  AgentConfigurationDetailsClient agentConfigurationDetailsClient(ResourceLoader resourceLoader) {
    return new AgentConfigurationDetailsClient(resourceLoader, new Yaml());
  }

  @Bean
  AgentsRegistry agentRegistry(
      AgentDiscovery agentDiscovery,
      AgentDetailsClient agentDetailsClient,
      AgentConfigurationDetailsClient agentConfigurationDetailsClient) {

    return new AgentsRegistry(agentDiscovery, agentDetailsClient, agentConfigurationDetailsClient);
  }

  @Bean
  AgentMappingService agentMappingService(AgentsRegistry agentsRegistry) {
    return new AgentMappingService(agentsRegistry);
  }

  @Bean
  AgentQuery agentQuery(AgentsRegistry agentsRegistry) {
    return new AgentQuery(agentsRegistry);
  }
}
