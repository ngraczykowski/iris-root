package com.silenteight.customerbridge.common.batch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.customerbridge.cbs.gateway.CbsAckAlert;
import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AlertCompositeToAckAlertMapper {

  static CbsAckAlert toAckAlert(@NonNull ScbAlertDetails scbAlertDetails) {

    return new CbsAckAlert(
        scbAlertDetails.getSystemId(),
        scbAlertDetails.getBatchId(),
        isWatchlistLevel(scbAlertDetails.getWatchlistId()));
  }

  private static boolean isWatchlistLevel(String watchlistId) {
    return isNotBlank(watchlistId);
  }
}
