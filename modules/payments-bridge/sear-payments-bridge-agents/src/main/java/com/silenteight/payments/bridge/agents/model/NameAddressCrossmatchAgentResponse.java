package com.silenteight.payments.bridge.agents.model;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value(staticConstructor = "of")
public class NameAddressCrossmatchAgentResponse {

  @NonNull Result result;
  @NonNull Map<String, String> apProperties;

  public enum Result {
    NO_DECISION, CROSSMATCH, NO_CROSSMATCH
  }
}
