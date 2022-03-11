package com.silenteight.scb.ingest.adapter.incomming.common.model.alert;

import lombok.Builder;

public record AlertDetails(
    String batchId,
    String unit,
    String account,
    String systemId,
    String watchlistId,
    String alertName,
    String recordDetails) {

  @Builder
  public AlertDetails {}
}
