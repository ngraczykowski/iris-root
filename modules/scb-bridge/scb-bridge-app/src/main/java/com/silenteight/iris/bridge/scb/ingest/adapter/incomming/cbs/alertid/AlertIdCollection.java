/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class AlertIdCollection {

  @NonNull
  private final List<AlertId> alertIds;
  @ToString.Include
  private final AlertIdContext context;

  @ToString.Include
  public int getSize() {
    return alertIds.size();
  }
}
