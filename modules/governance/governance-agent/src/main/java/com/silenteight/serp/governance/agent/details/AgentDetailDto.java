package com.silenteight.serp.governance.agent.details;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AgentDetailDto {

  @NonNull
  List<String> features;
  @NonNull
  List<String> responses;
}
