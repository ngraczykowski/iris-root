/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class AlertIdContext {

  private final boolean ackRecords;
  private final String hitDetailsView;
  private final int priority;
  private final boolean watchlistLevel;
  @NonNull
  private final String recordsView;

}
