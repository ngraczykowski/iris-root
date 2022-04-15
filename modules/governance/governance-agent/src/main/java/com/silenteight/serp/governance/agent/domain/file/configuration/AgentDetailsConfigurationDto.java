package com.silenteight.serp.governance.agent.domain.file.configuration;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class AgentDetailsConfigurationDto {

  @NonNull
  String name;
  @NonNull
  String configFile;
}
