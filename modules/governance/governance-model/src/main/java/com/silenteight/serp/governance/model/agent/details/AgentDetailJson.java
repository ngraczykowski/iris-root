package com.silenteight.serp.governance.model.agent.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
class AgentDetailJson {

  @NonNull
  List<String> features;
  @NonNull
  List<String> responses;

  AgentDetailDto toDto() {
    return AgentDetailDto.builder()
        .features(this.getFeatures())
        .responses(this.getResponses())
        .build();
  }
}
