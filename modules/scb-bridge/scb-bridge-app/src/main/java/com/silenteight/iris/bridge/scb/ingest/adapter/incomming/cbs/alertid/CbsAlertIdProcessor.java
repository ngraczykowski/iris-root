/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@AllArgsConstructor
public class CbsAlertIdProcessor {

  private final List<AlertId> alertsToBeProcessed = new ArrayList<>();
  private final AlertIdContext context;
  private final int batchSize;
  private final Consumer<AlertIdCollection> consumer;

  public void process(AlertId alertId) {
    alertsToBeProcessed.add(alertId);

    if (alertsToBeProcessed.size() >= batchSize)
      sendAlertIds();
  }

  private void sendAlertIds() {
    log.info("Publishing collected {} alerts to be processed", alertsToBeProcessed.size());
    consumer.accept(new AlertIdCollection(alertsToBeProcessed, context));
    alertsToBeProcessed.clear();
  }

  public void processRemaining() {
    if (!alertsToBeProcessed.isEmpty())
      sendAlertIds();
  }
}
