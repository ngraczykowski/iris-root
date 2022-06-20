/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.event;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AckCalledEvent {

  boolean watchlistLevel;
  String statusCode;
}
