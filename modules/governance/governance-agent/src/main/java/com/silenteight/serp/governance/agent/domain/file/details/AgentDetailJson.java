package com.silenteight.serp.governance.agent.domain.file.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

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
  List<String> responses;
  @NonNull
  List<AgentDetailsConfigurationDto> configurations;

  AgentDetailDto toDto(@NonNull String agentName) {
    return AgentDetailDto.builder()
        .id(this.getAgentId())
        .name(agentName)
        .features(this.getFeatures())
        .responses(this.getResponses())
        .configurations(this.getConfigurations())
        .build();
  }
}
