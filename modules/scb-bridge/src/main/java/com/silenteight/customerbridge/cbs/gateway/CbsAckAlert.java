package com.silenteight.customerbridge.cbs.gateway;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@Builder
@EqualsAndHashCode
public class CbsAckAlert {

  private String alertExternalId;
  private String batchId;
  private boolean watchlistLevel;

  public CbsAckAlert(String alertExternalId, String batchId) {
    validateInput(alertExternalId, batchId);

    this.alertExternalId = alertExternalId;
    this.batchId = batchId;
  }

  public CbsAckAlert(String alertExternalId, String batchId, boolean watchlistLevel) {
    validateInput(alertExternalId, batchId);

    this.alertExternalId = alertExternalId;
    this.batchId = batchId;
    this.watchlistLevel = watchlistLevel;
  }

  private static void validateInput(String alertExternalId, String batchId) {
    if (isBlank(alertExternalId) || isBlank(batchId))
      throw new IllegalArgumentException("AlertExternalId and batchId cannot be empty");
  }
}
