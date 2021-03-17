package com.silenteight.serp.governance.model.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.model.agent.config.AgentConfigDto;
import com.silenteight.serp.governance.model.agent.config.AgentDiscovery;
import com.silenteight.serp.governance.model.agent.details.AgentDetailDto;
import com.silenteight.serp.governance.model.agent.details.AgentDetailsClient;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class AgentsRegistry {

  private final AgentDiscovery agentDiscovery;
  private final AgentDetailsClient agentDetailsClient;

  public List<AgentDto> getAllAgents() {
    return agentDiscovery
        .getAgentConfigs()
        .stream()
        .map(this::getAgentDetails)
        .collect(toList());
  }

  public Optional<AgentDto> getSingleAgent(String agentName) {
    return agentDiscovery
        .getAgentConfigs()
        .stream()
        .filter(agent -> agent.hasName(agentName))
        .map(this::getAgentDetails)
        .findFirst();
  }

  private AgentDto getAgentDetails(AgentConfigDto agentConfigDto) {
    AgentDetailDto agentDetailDto = agentDetailsClient
        .getAgentDetailsForAgentConfig(agentConfigDto);

    return AgentDto.builder()
        .name(agentConfigDto.getName())
        .features(agentDetailDto.getFeatures())
        .solutions(agentDetailDto.getResponses())
        .build();
  }
}
