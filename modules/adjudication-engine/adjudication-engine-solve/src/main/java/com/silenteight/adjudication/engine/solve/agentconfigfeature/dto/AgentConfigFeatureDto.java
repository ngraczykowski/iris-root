package com.silenteight.adjudication.engine.solve.agentconfigfeature.dto;

import lombok.Builder;
import lombok.Value;

@Value
public class AgentConfigFeatureDto {

  long id;

  String agentConfig;

  String feature;
}
