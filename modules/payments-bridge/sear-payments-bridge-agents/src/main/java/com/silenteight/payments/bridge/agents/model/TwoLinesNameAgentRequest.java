package com.silenteight.payments.bridge.agents.model;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class TwoLinesNameAgentRequest {

  @NonNull List<String> alertedPartyAddresses;
}
