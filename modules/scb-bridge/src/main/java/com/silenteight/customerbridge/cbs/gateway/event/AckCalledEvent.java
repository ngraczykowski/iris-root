package com.silenteight.customerbridge.cbs.gateway.event;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AckCalledEvent {

  boolean watchlistLevel;
  String statusCode;
}
