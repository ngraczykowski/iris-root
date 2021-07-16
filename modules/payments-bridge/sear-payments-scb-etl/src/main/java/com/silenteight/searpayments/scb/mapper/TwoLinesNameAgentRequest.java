package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class TwoLinesNameAgentRequest {

  @NonNull List<String> alertedPartyAddresses;
}
