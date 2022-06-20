/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.metrics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
class MeterFetchedAlertsListener {

  private final BridgeMetrics bridgeMetrics;

  @Async
  @EventListener
  public void onEvent(AlertsFetchedEvent event) {
    try {
      bridgeMetrics.alertsFetched(event.getChunkSize());
    } catch (Exception cause) {
      log.error("Cannot meter fetched alerts. Cause: ", cause);
    }
  }

}
