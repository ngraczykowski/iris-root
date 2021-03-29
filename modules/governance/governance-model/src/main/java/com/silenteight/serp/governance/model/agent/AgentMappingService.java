package com.silenteight.serp.governance.model.agent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AgentMappingService {

  @NonNull
  private final AgentsRegistry agentsRegistry;

  public FeaturesListDto getFeaturesListDto() {
    return mapAgentsDtoIntoFeaturesDto(agentsRegistry.getAllAgents());
  }

  private FeaturesListDto mapAgentsDtoIntoFeaturesDto(List<AgentDto> agentDtos) {
    List<FeatureDto> featuresDto = agentDtos.stream()
        .flatMap(this::mapSingleAgent)
        .collect(toList());

    return FeaturesListDto.builder()
        .agents(featuresDto)
        .build();
  }

  private Stream<FeatureDto> mapSingleAgent(AgentDto agentDto) {
    List<String> solutions = agentDto.getSolutions();
    return agentDto.getFeatures().stream()
        .map(feature -> getFeatureDto(solutions, feature));
  }

  private FeatureDto getFeatureDto(List<String> solutions, String feature) {
    return FeatureDto.builder()
        .name(feature)
        .solutions(solutions)
        .build();
  }
}
