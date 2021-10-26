package com.silenteight.payments.bridge.agents.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CompanyNameSurroundingAgentResponse {
  NO_MATCH,
  MATCH_1,
  MATCH_2,
  MATCH_3,
  MATCH_4,
  MATCH_5_OR_MORE;

  public static List<String> getValues() {
    return Stream.of(CompanyNameSurroundingAgentResponse.values())
        .map(CompanyNameSurroundingAgentResponse::name)
        .collect(Collectors.toList());
  }
}
