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
  MATCH_5_OR_MORE,
  AGENT_ERROR;

  public static List<String> getValues() {
    return Stream.of(CompanyNameSurroundingAgentResponse.values())
        .map(CompanyNameSurroundingAgentResponse::name)
        // XXX(ahaczewski): Do not count AGENT_ERROR towards category values, breaking the
        //  protobuf contract, BUT this is to prevent AGENT_ERROR showing up as a value to choose
        //  from in Policy Editor.
        .filter(s -> !s.contains("ERROR"))
        .collect(Collectors.toList());
  }
}
