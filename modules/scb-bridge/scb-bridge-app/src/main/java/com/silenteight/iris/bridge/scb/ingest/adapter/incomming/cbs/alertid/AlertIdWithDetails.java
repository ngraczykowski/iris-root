/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;

import javax.validation.constraints.NotNull;

@Builder
@Value
public class AlertIdWithDetails {

  @NonNull
  String systemId;

  @NotNull
  String batchId;

  @NonNull
  ScbAlertIdContext context;

  public AlertId toAlertId() {
    return new AlertId(systemId, batchId);
  }
}
