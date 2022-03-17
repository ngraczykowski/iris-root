package com.silenteight.scb.ingest.adapter.incomming.cbs.reactive;

import lombok.Builder;
import lombok.Value;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;

@Value
@Builder
public class AckAlertRequest {

  AlertId alertId;
  boolean watchlistLevel;

  String getSystemId() {
    return alertId.getSystemId();
  }

  CbsAckAlert asCbsAckAlert() {
    return CbsAckAlert.builder()
        .alertExternalId(alertId.getSystemId())
        .batchId(alertId.getBatchId())
        .watchlistLevel(watchlistLevel)
        .build();
  }
}
