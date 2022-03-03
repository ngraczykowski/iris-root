package com.silenteight.customerbridge.cbs.reactive;

import lombok.Value;

import com.silenteight.customerbridge.cbs.gateway.CbsOutput.State;

@Value
public class AckAlertResult {

  State state;
  long index;
}
