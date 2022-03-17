package com.silenteight.scb.ingest.adapter.incomming.cbs.reactive;

import lombok.Value;

import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsOutput.State;

@Value
public class AckAlertResult {

  State state;
  long index;
}
