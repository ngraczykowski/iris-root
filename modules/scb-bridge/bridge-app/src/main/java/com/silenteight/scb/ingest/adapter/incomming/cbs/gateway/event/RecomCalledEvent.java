package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.event;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RecomCalledEvent {

  String statusCode;
}
