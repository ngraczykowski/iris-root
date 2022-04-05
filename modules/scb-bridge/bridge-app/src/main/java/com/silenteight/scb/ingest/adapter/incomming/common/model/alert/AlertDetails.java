package com.silenteight.scb.ingest.adapter.incomming.common.model.alert;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
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
