/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
public class AlertId {

  @NonNull
  String systemId;

  @NotNull
  @Size(min = 1)
  String batchId;

  public AlertId(String systemId, String batchId) {
    this.systemId = systemId;
    this.batchId = batchId;
  }
}

