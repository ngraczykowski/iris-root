package com.silenteight.serp.governance.agent.list.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.serp.governance.agent.details.dto.FeatureDto;

import java.util.List;
import javax.validation.constraints.NotNull;

@Value
@Builder
@RequiredArgsConstructor
public class ListAgentDto {

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
}
