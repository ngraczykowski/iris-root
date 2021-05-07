package com.silenteight.serp.governance.agent.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
class AgentConfigJson {

  @NonNull
  String name;

  AgentConfigDto toDto() {
    return new AgentConfigDto(this.getName());
  }
}
