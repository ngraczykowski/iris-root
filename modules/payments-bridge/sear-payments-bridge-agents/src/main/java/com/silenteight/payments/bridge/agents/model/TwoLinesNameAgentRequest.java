package com.silenteight.payments.bridge.agents.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TwoLinesNameAgentRequest {

  @NonNull List<String> alertedPartyAddresses;
}
