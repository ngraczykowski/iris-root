package com.silenteight.serp.governance.agent.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.agent.domain.dto.AgentDto;
import com.silenteight.serp.governance.agent.feature.FeatureDto;
import com.silenteight.serp.governance.agent.feature.FeaturesListDto;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class AgentMappingService {

  @NonNull
  private final AgentsRegistry agentsRegistry;

  public FeaturesListDto getFeaturesListDto() {
    return mapAgentsDtoIntoFeaturesDto(agentsRegistry.getAllAgents());
  }

  private static FeaturesListDto mapAgentsDtoIntoFeaturesDto(List<AgentDto> agentDtos) {
    List<FeatureDto> featuresDto = agentDtos.stream()
        .flatMap(AgentMappingService::mapSingleAgent)
        .collect(toList());

    return FeaturesListDto.builder()
        .features(featuresDto)
        .build();
  }

  private static Stream<FeatureDto> mapSingleAgent(AgentDto agentDto) {
    List<String> solutions = agentDto.getSolutions();
    return agentDto.getFeatures().stream()
        .map(feature -> getFeatureDto(solutions, feature));
  }

  private static FeatureDto getFeatureDto(List<String> solutions, String feature) {
    return FeatureDto.builder()
        .name(feature)
        .solutions(solutions)
        .build();
  }
}
