package com.silenteight.serp.governance.model.agent;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AgentDto {

  @NonNull
  String name;
  @NonNull
  List<String> solutions;
}
