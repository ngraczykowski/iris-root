package com.silenteight.serp.governance.agent.domain.file.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.serp.governance.agent.details.dto.FeatureDto;
import com.silenteight.serp.governance.agent.domain.file.configuration.AgentDetailsConfigurationDto;

import java.util.List;

@Value
@RequiredArgsConstructor
class AgentDetailJson {

  @NonNull
  String agentId;
  @NonNull
  List<String> features;
  @NonNull
  List<FeatureDto> featuresList;
  @NonNull
  List<String> responses;
  @NonNull
  List<AgentDetailsConfigurationDto> configurations;

  AgentDetailDto toDto(@NonNull String agentName) {
    return AgentDetailDto.builder()
        .id(getAgentId())
        .name(agentName)
        .features(getFeatures())
        .featuresList(getFeaturesList())
        .responses(getResponses())
        .configurations(getConfigurations())
        .build();
  }
}
