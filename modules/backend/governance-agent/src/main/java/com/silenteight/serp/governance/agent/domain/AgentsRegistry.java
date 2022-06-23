package com.silenteight.serp.governance.agent.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.agent.domain.dto.AgentDto;
import com.silenteight.serp.governance.agent.domain.file.config.AgentConfigDto;
import com.silenteight.serp.governance.agent.domain.file.config.AgentDiscovery;
import com.silenteight.serp.governance.agent.domain.file.configuration.AgentConfigurationDetailsClient;
import com.silenteight.serp.governance.agent.domain.file.configuration.AgentDetailsConfigurationDto;
import com.silenteight.serp.governance.agent.domain.file.configuration.UnreachableConfigurationException;
import com.silenteight.serp.governance.agent.domain.file.details.AgentDetailDto;
import com.silenteight.serp.governance.agent.domain.file.details.AgentDetailsClient;
import com.silenteight.serp.governance.agent.domain.file.details.UnreachableAgentException;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class AgentsRegistry {

  private final AgentDiscovery agentDiscovery;
  private final AgentDetailsClient agentDetailsClient;
  private final AgentConfigurationDetailsClient agentConfigurationDetailsClient;

  public List<AgentDto> getAllAgents() {
    return agentDiscovery
        .getAgentConfigs()
        .stream()
        .map(this::getAgentDto)
        .collect(toList());
  }

  public Optional<AgentDto> getSingleAgent(String agentName) {
    return agentDiscovery
        .getAgentConfigs()
        .stream()
        .filter(agent -> agent.hasName(agentName))
        .map(this::getAgentDto)
        .findFirst();
  }

  private AgentDto getAgentDto(AgentConfigDto agentConfigDto) {
    AgentDetailDto agentDetailDto = agentDetailsClient
        .getAgentDetailsForAgentConfig(agentConfigDto);

    return AgentDto.builder()
        .id(agentDetailDto.getId())
        .name(agentConfigDto.getName())
        .features(agentDetailDto.getFeatures())
        .featuresList(agentDetailDto.getFeaturesList())
        .solutions(agentDetailDto.getResponses())
        .build();
  }

  public String getAgentConfigurationDetails(String agentId, String configurationName) {
    AgentDetailDto agentDetailDto = getSingleAgentById(agentId);
    AgentDetailsConfigurationDto configurationDto = getAgentDetailsConfigurationDto(
        agentDetailDto, configurationName);

    return agentConfigurationDetailsClient.getDetails(configurationDto.getConfigFile());
  }

  private AgentDetailsConfigurationDto getAgentDetailsConfigurationDto(
      AgentDetailDto agentDetailDto,
      String configurationName) {

    return agentDetailDto.getConfigurations()
        .stream()
        .filter(configuration -> configuration.getName().equals(configurationName))
        .findFirst()
        .orElseThrow(() -> new UnreachableConfigurationException(configurationName));
  }

  public AgentDetailDto getSingleAgentById(String id) {
    return agentDiscovery.getAgentConfigs()
        .stream()
        .map(agentDetailsClient::getAgentDetailsForAgentConfig)
        .filter(e -> e.getId().equals(id))
        .findFirst().orElseThrow(() -> new UnreachableAgentException(id));
  }
}
