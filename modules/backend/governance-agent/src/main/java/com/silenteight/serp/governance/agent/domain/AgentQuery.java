package com.silenteight.serp.governance.agent.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.agent.details.AgentDetailsQuery;
import com.silenteight.serp.governance.agent.details.dto.AgentDetailsDto;
import com.silenteight.serp.governance.agent.domain.dto.AgentDto;
import com.silenteight.serp.governance.agent.domain.file.details.AgentDetailDto;
import com.silenteight.serp.governance.agent.list.ListAgentQuery;
import com.silenteight.serp.governance.agent.list.dto.ListAgentDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AgentQuery implements AgentDetailsQuery, ListAgentQuery {

  private final AgentsRegistry agentsRegistry;

  @Override
  public AgentDetailsDto details(@NonNull String id) {
    AgentDetailDto agentDetailDto = agentsRegistry.getSingleAgentById(id);
    return AgentDetailsDto
        .builder()
        .id(agentDetailDto.getId())
        .name(AgentDetailsResolver.resolveName(agentDetailDto.getId()))
        .agentName(AgentDetailsResolver.resolveAgentName(agentDetailDto.getName()))
        .agentVersion(AgentDetailsResolver.resolveAgentVersion(agentDetailDto.getName()))
        .configurations(
            AgentDetailsResolver.resolveConfigurations(agentDetailDto.getConfigurations()))
        .responses(agentDetailDto.getResponses())
        .features(agentDetailDto.getFeatures())
        .featuresList(agentDetailDto.getFeaturesList())
        .build();
  }

  @Override
  public List<ListAgentDto> list() {
    return agentsRegistry
        .getAllAgents()
        .stream()
        .map(this::toListAgentDto)
        .collect(toList());
  }

  private ListAgentDto toListAgentDto(AgentDto agentDto) {
    return ListAgentDto
        .builder()
        .id(agentDto.getId())
        .name(AgentDetailsResolver.resolveName(agentDto.getId()))
        .agentName(AgentDetailsResolver.resolveAgentName(agentDto.getName()))
        .agentVersion(AgentDetailsResolver.resolveAgentVersion(agentDto.getName()))
        .features(agentDto.getFeatures())
        .featuresList(agentDto.getFeaturesList())
        .build();
  }
}
