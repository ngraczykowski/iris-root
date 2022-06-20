/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway.CbsAckAlert;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class AlertCompositeToAckAlertMapper {

  static CbsAckAlert toAckAlert(@NonNull AlertDetails alertDetails) {

    return new CbsAckAlert(
        alertDetails.getSystemId(),
        alertDetails.getBatchId(),
        isWatchlistLevel(alertDetails.getWatchlistId()));
  }

  private static boolean isWatchlistLevel(String watchlistId) {
    return isNotBlank(watchlistId);
  }
}
