package com.silenteight.serp.governance.model.agent.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
class AgentConfigsWrapperJson {

  @NonNull
  List<AgentConfigJson> agentConfigs;
}
