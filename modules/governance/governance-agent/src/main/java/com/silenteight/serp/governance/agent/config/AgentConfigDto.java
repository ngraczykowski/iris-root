package com.silenteight.serp.governance.agent.config;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
public class AgentConfigDto {

  @NonNull
  String name;

  public boolean hasName(String agentName) {
    return name.equals(agentName);
  }
}
