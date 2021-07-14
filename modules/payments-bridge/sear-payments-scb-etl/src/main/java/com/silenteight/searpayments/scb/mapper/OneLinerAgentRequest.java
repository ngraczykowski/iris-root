package com.silenteight.searpayments.scb.mapper;

import lombok.Value;

@Value
public class OneLinerAgentRequest {

  boolean noAcctNumFlag;
  int noOfLines;
  int messageLength;
}
