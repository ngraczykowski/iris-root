package com.silenteight.customerbridge.cbs.gateway;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.protocol.AlertWrapper;
import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.v1.alert.Alert;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@RequiredArgsConstructor
public class CbsRecommendationServiceSelector {

  private final CbsRecommendationService alertLevelRecommendationService;
  private final CbsRecommendationService watchlistLevelRecommendationService;

  public CbsRecommendationService getRecommendationService(Alert alert) {
    return isAlertLevelProcessing(alert) ?
           alertLevelRecommendationService :
           watchlistLevelRecommendationService;
  }

  private static boolean isAlertLevelProcessing(Alert alert) {
    AlertWrapper alertWrapper = new AlertWrapper(alert);
    ScbAlertDetails details = alertWrapper.unpackDetails(ScbAlertDetails.class)
        .orElseThrow(() -> new IllegalArgumentException("Alert has no details"));
    return isEmpty(details.getWatchlistId());
  }
}
