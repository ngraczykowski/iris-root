package com.silenteight.serp.governance.agent.domain.file.details;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.agent.details.dto.FeatureDto;
import com.silenteight.serp.governance.agent.domain.file.configuration.AgentDetailsConfigurationDto;

import java.util.List;

@Value
@Builder
public class AgentDetailDto {

  @NonNull
  String id;
  @NonNull
  String name;
  @NonNull
  List<String> features;
  @NonNull
  List<FeatureDto> featuresList;
  @NonNull
  List<String> responses;
  @NonNull
  List<AgentDetailsConfigurationDto> configurations;
}
