package com.silenteight.serp.governance.agent.details.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import javax.validation.constraints.NotNull;

@Builder
@Value
public class AgentDetailsDto {

  @NotNull
  String id;
  @NotNull
  String name;
  @NotNull
  String agentName;
  @NotNull
  String agentVersion;
  @NotNull
  List<String> features;
  @NotNull
  List<FeatureDto> featuresList;
  @NotNull
  List<String> responses;
  @NotNull
  List<String> configurations;
}
