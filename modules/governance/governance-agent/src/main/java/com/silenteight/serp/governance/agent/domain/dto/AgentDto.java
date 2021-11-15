package com.silenteight.serp.governance.agent.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.agent.details.dto.FeatureDto;

import java.util.List;

@Value
@Builder
public class AgentDto {

  @NonNull
  String id;
  @NonNull
  String name;
  @NonNull
  List<String> features;
  @NonNull
  List<FeatureDto> featuresList;
  @NonNull
  List<String> solutions;
}
