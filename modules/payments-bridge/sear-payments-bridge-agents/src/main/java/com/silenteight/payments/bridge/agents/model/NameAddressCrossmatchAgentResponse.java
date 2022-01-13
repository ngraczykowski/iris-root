package com.silenteight.payments.bridge.agents.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum NameAddressCrossmatchAgentResponse {
  NO_DECISION,
  CROSSMATCH,
  NO_CROSSMATCH;

  public static List<String> getValues() {
    return Stream.of(NameAddressCrossmatchAgentResponse.values())
        .map(NameAddressCrossmatchAgentResponse::name)
        .collect(Collectors.toList());
  }
}
