package com.silenteight.serp.governance.agent.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.agent.details.dto.FeatureDetailsDto;
import com.silenteight.serp.governance.agent.domain.dto.AgentDto;
import com.silenteight.serp.governance.agent.domain.dto.FeatureDto;
import com.silenteight.serp.governance.agent.domain.dto.FeaturesListDto;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AgentMappingService implements FeaturesProvider {

  @NonNull
  private final AgentsRegistry agentsRegistry;

  @Override
  public FeaturesListDto getFeaturesListDto() {
    return mapAgentsDtoIntoFeaturesDto(agentsRegistry.getAllAgents());
  }

  private static FeaturesListDto mapAgentsDtoIntoFeaturesDto(List<AgentDto> agentDtos) {
    List<FeatureDto> featuresDto = agentDtos
        .stream()
        .flatMap(AgentMappingService::mapSingleAgent)
        .collect(toList());

    return FeaturesListDto.builder()
        .features(featuresDto)
        .build();
  }

  private static Stream<FeatureDto> mapSingleAgent(AgentDto agentDto) {
    return agentDto.getFeaturesList().stream()
        .map(featureDetailsDto -> toFeatureDto(featureDetailsDto, agentDto));
  }

  private static FeatureDto toFeatureDto(FeatureDetailsDto featureDetailsDto, AgentDto agentDto) {
    return FeatureDto.builder()
        .name(featureDetailsDto.getName())
        .displayName(featureDetailsDto.getDisplayName())
        .agentConfig(agentDto.getName())
        .solutions(agentDto.getSolutions())
        .build();
  }
}
