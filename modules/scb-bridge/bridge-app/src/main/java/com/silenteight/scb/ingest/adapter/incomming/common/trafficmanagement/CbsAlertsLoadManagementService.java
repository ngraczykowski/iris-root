package com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CbsAlertsLoadManagementService {

  private final AlertInFlightService alertInFlightService;
  private final CbsAlertsLoadManagementProperties loadManagementProperties;
  private boolean ready = true;

  boolean isReadyToLoad() {
    if (!loadManagementProperties.enabled()) {
      return true;
    }

    var ackAlertsCount = alertInFlightService.getAckAlertsCount();
    if (ready) {
      if (aboveMaxThreshold(ackAlertsCount)) {
        log.info("Max load threshold has been exceeded");
        ready = false;
      }
    } else {
      if (belowFairThreshold(ackAlertsCount)) {
        log.info("Fair load threshold has been achieved");
        ready = true;
      }
    }
    return ready;
  }

  private boolean aboveMaxThreshold(long ackAlertsCount) {
    return ackAlertsCount >= loadManagementProperties.maxLoadThreshold();
  }

  private boolean belowFairThreshold(long ackAlertsCount) {
    return ackAlertsCount <= loadManagementProperties.fairLoadThreshold();
  }
}
