package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import com.silenteight.customerbridge.cbs.alertid.AlertId;


@Value
public class InvalidAlert {

  String systemId;
  String batchId;
  Reason invalidityReason;

  AlertId getAlertId() {
    return AlertId.builder()
        .systemId(systemId)
        .batchId(batchId)
        .build();
  }

  String getReasonMessage() {
    return invalidityReason.getReasonMessage();
  }

  boolean hasReasonCausedByFatalError() {
    return invalidityReason != Reason.TEMPORARILY_UNAVAILABLE;
  }

  @RequiredArgsConstructor
  @Getter
  public enum Reason {

    ABSENT("Alert was absent"),
    DAMAGED("Alert is damaged"),
    WRONG_BATCH_ID("Batch ID was different than expected"),
    TEMPORARILY_UNAVAILABLE("Alert was temporarily unavailable"),
    FAILED_TO_FETCH("Failed to fetch the alert from database");

    private final String reasonMessage;
  }
}
