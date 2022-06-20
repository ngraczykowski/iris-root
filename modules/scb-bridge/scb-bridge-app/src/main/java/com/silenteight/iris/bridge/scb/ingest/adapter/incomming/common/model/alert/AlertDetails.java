/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class AlertDetails {

  private final String batchId;
  private final String internalBatchId;
  private final String unit;
  private final String account;
  private final String systemId;
  private final String watchlistId;
  private final String recordDetails;
  @Setter
  private String alertName;
}
