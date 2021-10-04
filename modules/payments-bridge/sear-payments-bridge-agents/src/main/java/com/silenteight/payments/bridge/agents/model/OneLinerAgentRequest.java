package com.silenteight.payments.bridge.agents.model;

import lombok.Value;

@Value
public class OneLinerAgentRequest {

  boolean noAcctNumFlag;
  int noOfLines;
  int messageLength;
}
