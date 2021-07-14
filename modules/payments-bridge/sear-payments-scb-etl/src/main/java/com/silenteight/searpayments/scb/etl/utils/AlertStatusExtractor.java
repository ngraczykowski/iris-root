package com.silenteight.searpayments.scb.etl.utils;

import com.silenteight.searpayments.bridge.dto.input.StatusDto;
import com.silenteight.searpayments.scb.etl.response.AlertEtlResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertStatusExtractor {

  public static AlertEtlResponse.AlertStatus retrieveAlertStatus(StatusDto currentStatus) {
    return new AlertEtlResponse.AlertStatus(
        currentStatus.getId(),
        currentStatus.getName(),
        currentStatus.getChecksum(),
        currentStatus.getRoutingCode());
  }
}
